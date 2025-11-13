package com.vroomvroom.delivery.infrastructure;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.entity.RouteManagerAssignment;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.AssignmentQueuePort;
import com.vroomvroom.delivery.domain.repository.DeliveryManagerRepository;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubAssignmentHandler {

    @Value("${app.redis.keys.manager-queue}")
    private String managerQueueKey;

    private final AssignmentQueuePort assignmentQueuePort;
    private final DeliveryManagerRepository managerRepository;
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void assignForHubManager(UUID routeId) {
        log.info("허브매니저 배정 요청 수신. routeId = {}", routeId);

        DeliveryRoute route = deliveryRepository.findByRouteId(routeId)
            .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        if (route.getDeliveryManagerId() != null) { // 이미 배정된 상태
            log.warn("허브매니저가 이미 배정된 경로입니다. routeId = {}", routeId);
            return;
        }

        if (route.getStatus() != DeliveryRouteStatus.HUB_MOVE_WAITING) {
            log.warn("대기 상태가 아닌 경로는 배정 불가합니다. routeId = {}, status = {}",
                routeId, route.getStatus());
            return;
        }

        Optional<Long> poppedSequence = assignmentQueuePort.popHubManagerSequence(managerQueueKey);
        if (poppedSequence.isEmpty()) {
            log.warn("모든 HUB_MANAGER가 배송중입니다.");

            return;
        }

        final Long sequence = poppedSequence.get();
        log.info("곧 배정될 허브매니저 순번: {}", sequence);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == STATUS_ROLLED_BACK) { // 롤백시에 sequence 복구
                        assignmentQueuePort.pushHubManagerSequence(managerQueueKey, sequence);
                        log.info("트랜잭션 롤백으로 순번 복구 sequence = {}", sequence);
                    }
                }
            }
        );

        DeliveryManager manager = managerRepository
            .findBySequenceAndType(
                sequence, DeliveryManagerType.HUB_MANAGER)
            .orElseThrow(() -> {
                log.error("DB에 sequence가 {}인 담당자가 존재하지 않습니다.", sequence);
                return new CustomException(DeliveryErrorCode.ASSIGNMENT_DATA_INCONSISTENCY);
            });

        if (Boolean.TRUE.equals(manager.getIsActive())) {
            log.warn("담당자 {}가 현재 배송중입니다. 재시도 요청.", manager.getId());
            throw new CustomException(DeliveryErrorCode.MANAGER_NOT_AVAILABLE);
        }

        route.assignManager(manager.getId()); // route에 특정 manager 배정.
        RouteManagerAssignment.create(route, manager);

        log.info("허브매니저 배정 성공 routeId = {}, managerId = {}", route.getId(), manager.getId());
    }
}
