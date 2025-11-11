package com.vroomvroom.delivery.application.service.impl;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.application.service.DeliveryRouteService;
import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.entity.RouteManagerAssignment;
import com.vroomvroom.delivery.domain.event.ManagerAssignmentEvent;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.DeliveryAssignmentMessageSender;
import com.vroomvroom.delivery.domain.repository.DeliveryManagerRepository;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus;
import com.vroomvroom.delivery.domain.vo.DeliveryStatus;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private final DeliveryAssignmentMessageSender deliveryAssignmentMessageSender;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void assignManager(DeliveryRoute route, DeliveryManager manager) {
        if (route.getStatus() != DeliveryRouteStatus.HUB_MOVE_WAITING) { // 허브대기중일 때만 배정 가능
            throw new CustomException(DeliveryErrorCode.HUB_MOVE_WAITING);
        }

        route.assignManager(manager.getId()); // route에 특정 manager 배정.
        route.updateStatus(DeliveryRouteStatus.HUB_MOVING);
        manager.activate(); // 매니저 상태 변경(isActive) : false -> true
        route.getDelivery().updateStatus(DeliveryStatus.TRANSIT_HUB); // 배송 상태 변경
        route.getDelivery().updateStartTime();

        RouteManagerAssignment.create(route, manager);
    }

    @Override
    @Transactional
    public void updateDeliveryRouteStatus(UUID deliveryId, UUID routeId) {
        DeliveryRoute route = findRouteOrThrow(routeId);

        // 허브 이동중인 경우만 도착 상태로 변경 가능.
        if (route.getStatus() != DeliveryRouteStatus.HUB_MOVING) {
            throw new CustomException(DeliveryErrorCode.HUB_MOVING);
        }

        // 상태 변경
        route.updateStatus(DeliveryRouteStatus.HUB_ARRIVED);

        DeliveryManager manager = deliveryManagerRepository.findById(route.getDeliveryManagerId())
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_MANAGER_NOT_FOUND));

        manager.deactivate(); // true -> false(배송중아님)

        route.updateActual(
            Duration.between(
                route.getCurrentAssignmentCreatedAt().orElse(route.getCreatedAt()),
                LocalDateTime.now()).getSeconds(),
            route.getExpectedDistance()
        );
        Long managerSequence = manager.getSequence().getValue();

        Long nextSequence = route.getSequence().getValue() + 1;
        UUID nextDeliveryId = route.getDelivery().getId();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (managerSequence != null) { // 일 끝난 담당자는 다시 대기열로
                    redisTemplate.opsForList()
                        .rightPush(managerQueueKey, String.valueOf(managerSequence));
                    log.info("매니저 순번 큐로 복귀. sequence = {}", managerSequence);
                }
                triggerNextRouteAssignment(nextDeliveryId, nextSequence); // 다음 경로 배정
            }
        });
    }

    @Override
    public DeliveryRoute findRouteOrThrow(UUID routeId) {
        return deliveryRepository.findByRouteId(routeId)
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));
    }

    /**
     * 현재 경로 기준으로 다음 배송 경로가 존재하면 매니저 배정 이벤트 발행
     */
    private void triggerNextRouteAssignment(UUID deliveryId, Long nextSequence) {
        Optional<DeliveryRoute> nextRoute = deliveryRepository
            .findByDeliveryIdAndSequence(deliveryId, nextSequence);

        if (nextRoute.isPresent()) {
            UUID nextRouteId = nextRoute.get().getId();
            log.info("다음 경로 배정 요청 발행. nextRouteId = {}", nextRouteId);
            deliveryAssignmentMessageSender.send(deliveryId,
                new ManagerAssignmentEvent(nextRouteId));
        } else {
            log.info("허브-허브 배송의 마지막 경로 배정 완료. deliveryId = {}", deliveryId);
        }
    }
}
