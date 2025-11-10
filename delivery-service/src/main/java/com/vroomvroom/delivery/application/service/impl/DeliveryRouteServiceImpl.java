package com.vroomvroom.delivery.application.service.impl;

import com.vroomvroom.delivery.application.service.DeliveryRouteService;
import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.entity.RouteManagerAssignment;
import com.vroomvroom.delivery.domain.event.ManagerAssignmentEvent;
import com.vroomvroom.delivery.domain.exception.CustomException;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.DeliveryAssignmentMessageSender;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryRouteServiceImpl implements DeliveryRouteService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryAssignmentMessageSender deliveryAssignmentMessageSender;

    public DeliveryRoute getRouteOrThrow(UUID routeId) {
        return deliveryRepository.findByRouteId(routeId)
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));
    }

    public void assignManager(DeliveryRoute route, DeliveryManager manager) {
        route.assignManager(manager.getId()); // route에 특정 manager 배정.
        route.updateDeliveryManagerId(manager);
        manager.updateStatus(); // 매니저 상태 변경(isActive) : false -> true
        route.updateStatus();
        route.getDelivery().updateStartTime();
        RouteManagerAssignment.create(route, manager);
    }

    public void scheduleNextAfterCommit(DeliveryRoute route) {
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    triggerNextRouteAssignment(route);
                }
            });
    }

    public void triggerNextRouteAssignment(DeliveryRoute currentRoute) {
        Long currentSequence = currentRoute.getSequence().getValue();
        UUID deliveryId = currentRoute.getDelivery().getId();

        Optional<DeliveryRoute> nextRoute = deliveryRepository
            .findByDeliveryIdAndSequence(deliveryId, currentSequence + 1);

        if (nextRoute.isPresent()) {
            ManagerAssignmentEvent newEvent = new ManagerAssignmentEvent(nextRoute.get().getId());
            log.info("다음 경로 배정 요청 발행. nextRouteId = {}", newEvent.getRouteId());
            deliveryAssignmentMessageSender.send(deliveryId, newEvent);
        } else {
            log.info("허브-허브 배송의 마지막 경로 배정 완료. deliveryId = {}", deliveryId);
        }
    }
}
