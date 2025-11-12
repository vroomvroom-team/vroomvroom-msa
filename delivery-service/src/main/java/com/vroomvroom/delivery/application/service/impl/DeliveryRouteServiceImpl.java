package com.vroomvroom.delivery.application.service.impl;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.application.service.DeliveryRouteService;
import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.repository.DeliveryManagerRepository;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus;
import com.vroomvroom.delivery.domain.vo.DeliveryStatus;
import com.vroomvroom.delivery.infrastructure.HubAssignmentHandler;
import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRouteRes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryRouteServiceImpl implements DeliveryRouteService {

    @Value("${app.redis.keys.manager-queue:global:manager:queue}")
    private String managerQueueKey;

    private final DeliveryRepository deliveryRepository;
    private final DeliveryManagerRepository deliveryManagerRepository;
    private final HubAssignmentHandler assignmentHandler;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional
    public void startHub(UUID deliveryId, UUID routeId) {
        DeliveryRoute route = findRouteOrThrow(routeId);

        if (!route.getDelivery().getId().equals(deliveryId)) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_ID_MISMATCH);
        }

        if (route.getStatus() != DeliveryRouteStatus.HUB_MOVE_WAITING) {
            throw new CustomException(DeliveryErrorCode.HUB_MOVE_WAITING);
        }

        DeliveryManager manager = deliveryManagerRepository.findById(route.getDeliveryManagerId())
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_MANAGER_NOT_FOUND));

        if (Boolean.TRUE.equals(manager.getIsActive())) {
            throw new CustomException(DeliveryErrorCode.MANAGER_ALREADY_ACTIVE);
        }

        manager.activate();// 매니저 상태 변경(isActive) : false -> true
        route.updateStatus(DeliveryRouteStatus.HUB_MOVING);
        route.getDelivery().updateStatus(DeliveryStatus.TRANSIT_HUB); // 배송 상태 변경
        route.getDelivery().updateStartTime();
    }

    @Override
    @Transactional
    public void arriveHub(UUID deliveryId, UUID routeId) { // 허브 도착
        DeliveryRoute route = findRouteOrThrow(routeId);

        if (!route.getDelivery().getId().equals(deliveryId)) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_ID_MISMATCH);
        }

        // 허브 이동중인 경우만 도착 상태로 변경 가능.
        if (route.getStatus() != DeliveryRouteStatus.HUB_MOVING) {
            throw new CustomException(DeliveryErrorCode.HUB_MOVING);
        }

        DeliveryManager manager = deliveryManagerRepository.findById(route.getDeliveryManagerId())
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_MANAGER_NOT_FOUND));

        // 상태 변경
        route.updateStatus(DeliveryRouteStatus.HUB_ARRIVED);
        manager.deactivate(); // true -> false(배송중아님)

        route.updateActual(
            Duration.between(
                route.getCurrentAssignmentCreatedAt().orElse(route.getCreatedAt()),
                LocalDateTime.now()).getSeconds(),
            route.getExpectedDistance()
        );
        Long managerSequence = manager.getSequence().getValue();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (managerSequence != null) { // 일 끝난 담당자는 다시 대기열로
                    redisTemplate.opsForList()
                        .rightPush(managerQueueKey, String.valueOf(managerSequence));
                    log.info("매니저 순번 큐로 복귀. sequence = {}", managerSequence);
                }
                triggerNextRouteAssignment(route); // 다음 경로 배정
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetDeliveryRouteRes> getDeliveryAllRoute(UUID deliveryId) {
        List<DeliveryRoute> routes = deliveryRepository.findAllByDeliveryIdOrderBySequenceAsc(
            deliveryId);
        return routes.stream().map(GetDeliveryRouteRes::from).toList();
    }

    @Override
    public DeliveryRoute findRouteOrThrow(UUID routeId) {
        return deliveryRepository.findByRouteId(routeId)
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));
    }

    /**
     * 현재 경로 기준으로 다음 배송 경로가 존재하면 매니저 배정 이벤트 발행
     */
    private void triggerNextRouteAssignment(DeliveryRoute currentRoute) {
        Long nextSequence = currentRoute.getSequence().getValue() + 1;
        UUID deliveryId = currentRoute.getDelivery().getId();

        Optional<DeliveryRoute> nextRoute = deliveryRepository
            .findByDeliveryIdAndSequence(deliveryId, nextSequence);

        if (nextRoute.isPresent()) {
            UUID nextRouteId = nextRoute.get().getId();
            log.info("다음 경로 배정 요청 발행. nextRouteId = {}", nextRouteId);
            assignmentHandler.assignForHubManager(nextRouteId);
        } else {
            log.info("허브-허브 배송의 마지막 경로 배정 완료. deliveryId = {}", deliveryId);
        }
    }
}
