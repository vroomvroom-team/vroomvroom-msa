package com.vroomvroom.delivery.application.service.impl;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.application.command.CompleteDeliveryCommand;
import com.vroomvroom.delivery.application.command.CreateDeliveryCommand;
import com.vroomvroom.delivery.application.dto.GetDeliveryRoutesReq;
import com.vroomvroom.delivery.application.service.DeliveryManagerService;
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
import com.vroomvroom.delivery.infrastructure.external.dto.HubRouteDTO;
import com.vroomvroom.delivery.presentation.dto.response.CreateDeliveryRes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final DeliveryManagerService deliveryManagerService;
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

        // 경로 및 배송 생성
        List<DeliveryRoute> deliveryRoutes = createRoutesWithSequence(routes);
        Delivery delivery = Delivery.create(
            orderId, request.getStartHubId(), request.getArriveHubId(),
            deliveryRoutes, request.getAddress(),
            request.getReceiverId(), request.getReceiverSlackId()
        );
        deliveryRoutes.forEach(deliveryRoute ->
            deliveryRoute.attachToDelivery(delivery));

        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("배송 생성. deliveryId = {}", savedDelivery.getId());

        // 담당자 배정
        deliveryManagerService.assignManagerToDelivery(savedDelivery);

        return CreateDeliveryRes.from(savedDelivery);
    }

    @Override
    @Transactional
    public void handoffToCompany(UUID deliveryId) {
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
            } else {
                assignmentQueuePort.pushCompanyManagerSequence(hubId, sequence);
                log.warn("트랜잭션 없음. 큐 복귀. sequence = {}, hubId = {}", sequence, hubId);
            }
        }

        log.info("업체 배송 완료: deliveryId = {}", deliveryId);
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
}