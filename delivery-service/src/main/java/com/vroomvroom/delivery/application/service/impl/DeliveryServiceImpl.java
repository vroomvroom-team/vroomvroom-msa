package com.vroomvroom.delivery.application.service.impl;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.application.command.CompleteDeliveryCommand;
import com.vroomvroom.delivery.application.command.CreateDeliveryCommand;
import com.vroomvroom.delivery.application.dto.GetDeliveryRoutesReq;
import com.vroomvroom.delivery.application.service.DeliveryService;
import com.vroomvroom.delivery.domain.entity.Delivery;
import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.AssignmentQueuePort;
import com.vroomvroom.delivery.domain.port.HubClient;
import com.vroomvroom.delivery.domain.port.OrderClient;
import com.vroomvroom.delivery.domain.repository.DeliveryManagerRepository;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import com.vroomvroom.delivery.domain.vo.ArriveHubId;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteSequence;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus;
import com.vroomvroom.delivery.domain.vo.DeliveryStatus;
import com.vroomvroom.delivery.domain.vo.OrderId;
import com.vroomvroom.delivery.domain.vo.StartHubId;
import com.vroomvroom.delivery.infrastructure.HubAssignmentHandler;
import com.vroomvroom.delivery.infrastructure.external.dto.HubRouteDTO;
import com.vroomvroom.delivery.presentation.dto.response.CreateDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.GetAllDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryManagerRepository managerRepository;

    private final OrderClient orderClient;
    private final HubClient hubClient;
    private final HubAssignmentHandler hubAssignmentHandler;

    private static final Set<Integer> PERMITTED_PAGE_SIZES = Set.of(10, 30, 50);
    private static final int DEFAULT_PAGE_SIZE = 10;
    private final AssignmentQueuePort assignmentQueuePort;

    @Override
    @Transactional
    public CreateDeliveryRes createDelivery(CreateDeliveryCommand request) {
        // orderId 검증
        OrderId orderId = OrderId.of(request.getOrderId());
        orderClient.validateForDelivery(orderId.getId());

        // 허브-허브 경로들 가져오기
        List<HubRouteDTO> routes = hubClient.getRoutes(
            GetDeliveryRoutesReq.of(
                request.getStartHubId().getId(), request.getArriveHubId().getId()));
        log.info("HubClient 경로 가져오기 성공. routes = {}", routes);

        // 경로 및 배송 생성
        List<DeliveryRoute> deliveryRoutes = createRoutesWithSequence(routes);
        Delivery delivery = Delivery.create(
            orderId, request.getStartHubId(), request.getArriveHubId(),
            deliveryRoutes, request.getAddress(),
            request.getReceiverId(), request.getReceiverSlackId()
        );
        deliveryRoutes.forEach(deliveryRoute ->
            deliveryRoute.attachToDelivery(delivery));
        log.info("경로 및 배송 생성 = {}", delivery);

        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("배송 생성. deliveryId = {}", savedDelivery.getId());

        // 매니저가 배정될 첫번째 경로
        DeliveryRoute firstRoute = delivery.findFirstRoute()
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        log.info("첫번째 경로 = {}", firstRoute);

        // 첫번째 경로에 담당자 배정
        hubAssignmentHandler.assignForHubManager(firstRoute.getId());
        log.info("담당자 배정 성공. sequence = {}", firstRoute.getSequence());

        return CreateDeliveryRes.from(savedDelivery);
    }

    @Override
    @Transactional
    public void handoffToCompany(UUID deliveryId) { // 허브 -> 업체
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (delivery.getStatus() == DeliveryStatus.DELIVERY_IN_PROGRESS
            || delivery.getStatus() == DeliveryStatus.DELIVERY_COMPLETED) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_STATE_CONFLICT);
        }

        // deliveryId의 모든 경로가 허브에 도착하지 않았다면
        boolean allArrived = delivery.getDeliveryRoutes().stream()
            .allMatch(route -> route.getStatus() == DeliveryRouteStatus.HUB_ARRIVED);
        if (!allArrived) {
            throw new CustomException(DeliveryErrorCode.ROUTE_NOT_ARRIVED_YET);
        }

        // 배송에 배정된 배송경로 중 마지막
        DeliveryRoute lastRoute = delivery.getDeliveryRoutes().stream()
            .max(Comparator.comparing(route -> route.getSequence().getValue()))
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        UUID arriveHubId = lastRoute.getArriveHubId().getId();

        Optional<Long> poppedSequence = assignmentQueuePort.popCompanyManagerSequence(arriveHubId);
        if (poppedSequence.isEmpty()) {
            log.warn("모든 COMPANY_MANAGER가 배송중입니다.");
            delivery.updateStatus(DeliveryStatus.DELIVERY_PREPARING);
            return;
        }

        final Long sequence = poppedSequence.get();
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == STATUS_ROLLED_BACK) {
                        assignmentQueuePort.pushCompanyManagerSequence(arriveHubId, sequence);
                        log.info("트랜잭션 롤백으로 순번 복구 sequence = {}, hubId = {}",
                            sequence, arriveHubId);
                    }
                }
            });

        DeliveryManager manager = managerRepository
            .findAvailableManagerForAssignment(
                sequence, DeliveryManagerType.COMPANY_MANAGER, arriveHubId)
            .orElseThrow(() -> {
                log.error("[handoffToCompany] Redis와 DB 불일치. seq = {}", sequence);
                return new CustomException(DeliveryErrorCode.MANAGER_NOT_AVAILABLE);
            });

        // 상태 변경
        delivery.updateStatus(DeliveryStatus.DELIVERY_PREPARING);
        manager.activate();
        delivery.updateStatus(DeliveryStatus.DELIVERY_IN_PROGRESS);
        delivery.getDeliveryRoutes().forEach(route ->
            route.updateStatus(DeliveryRouteStatus.DELIVERY_IN_PROGRESS)
        );

        log.info("업체 배송 시작");
    }

    @Override
    @Transactional
    public void completeDelivery(UUID deliveryId, CompleteDeliveryCommand command) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (delivery.getStatus() != DeliveryStatus.DELIVERY_IN_PROGRESS) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_IN_PROGRESS_REQUIRED);
        }

        DeliveryManager manager = managerRepository.findById(command.getUserId())
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_MANAGER_NOT_FOUND));

        if (manager.getType() != DeliveryManagerType.COMPANY_MANAGER) {
            throw new CustomException(DeliveryErrorCode.INVALID_MANAGER_TYPE);
        }

        // 상태 변경
        delivery.updateStatus(DeliveryStatus.DELIVERY_COMPLETED);
        delivery.getDeliveryRoutes().forEach(route ->
            route.updateStatus(DeliveryRouteStatus.COMPLETED)
        );
        delivery.updateArriveTime();
        manager.deactivate();

        UUID hubId = (manager.getHubId() != null) ? manager.getHubId().getId() : null;
        Long sequence = (manager.getSequence() != null) ? manager.getSequence().getValue() : null;
        if (hubId != null && sequence != null) {
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            assignmentQueuePort.pushCompanyManagerSequence(hubId, sequence);
                            log.info("업체매니저 순번 큐로 복귀. sequence = {}", sequence);
                        }
                    });
            }
        }

        log.info("업체 배송 완료: deliveryId = {}", deliveryId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<GetAllDeliveryRes> getAllDelivery(Pageable pageable) {
        Pageable normalized = normalizedPageable(pageable);
        Page<Delivery> deliveries = deliveryRepository.findAllByDeletedAtIsNull(normalized);
        return PageResponse.fromPage(deliveries.map(GetAllDeliveryRes::from));
    }

    @Override
    @Transactional(readOnly = true)
    public GetDeliveryRes getDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        return GetDeliveryRes.from(delivery);
    }

    @Override
    @Transactional
    public void cancelDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (delivery.getStatus() != DeliveryStatus.HUB_WAITING) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_NOT_CANCEL);
        }
        delivery.updateStatus(DeliveryStatus.CANCELED);
    }

    private List<DeliveryRoute> createRoutesWithSequence(List<HubRouteDTO> routes) {
        List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

        int sequence = 0;
        for (HubRouteDTO route : routes) {
            DeliveryRoute routeEntity = DeliveryRoute.create(
                DeliveryRouteSequence.of((long) sequence++),
                StartHubId.of(route.getStartHubId()),
                ArriveHubId.of(route.getArriveHubId()),
                route.getDistance(),
                route.getTime(),
                DeliveryRouteStatus.HUB_MOVE_WAITING
            );
            deliveryRoutes.add(routeEntity);
        }

        return deliveryRoutes;
    }

    private Pageable normalizedPageable(Pageable pageable) {
        int size = PERMITTED_PAGE_SIZES.contains(
            pageable.getPageSize()) ? pageable.getPageSize() : DEFAULT_PAGE_SIZE;

        return PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
    }

}