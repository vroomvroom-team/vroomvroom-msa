package com.vroomvroom.delivery.infrastructure.consumer;

import com.vroomvroom.delivery.application.service.DeliveryRouteService;
import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.event.ManagerAssignmentEvent;
import com.vroomvroom.delivery.domain.exception.AllManagerBusyException;
import com.vroomvroom.delivery.domain.repository.DeliveryManagerRepository;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagerAssignmentConsumer {

    @Value("${app.redis.keys.manager-queue}")
    private String managerQueueKey;

    private final DeliveryManagerRepository managerRepository;
    private final DeliveryRouteService deliveryRouteService;
    private final StringRedisTemplate redisTemplate;

    @KafkaListener(
        topics = "${app.kafka.topics.manager-assignment}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleAssignment(ManagerAssignmentEvent event) {
        UUID routeId = event.getRouteId();
        log.info("담당자 배정 요청 수신. routeId = {}", routeId);

        try {
            DeliveryRoute route = deliveryRouteService.findRouteOrThrow(routeId);

            if (route.getDeliveryManagerId() != null) { // 이미 배정된 상태
                log.warn("배송담당자가 이미 배정된 경로입니다. routeId = {}", routeId);
                return;
            }

            if (route.getStatus() != DeliveryRouteStatus.HUB_MOVE_WAITING) {
                log.warn("대기 상태가 아닌 경로는 배정 불가합니다. routeId = {}, status = {}",
                    routeId, route.getStatus());
                return;
            }

            String poppedSequenceStr = redisTemplate.opsForList().leftPop(managerQueueKey);
            if (poppedSequenceStr == null) {
                log.warn("모든 담당자가 배송중입니다.");
                throw new AllManagerBusyException();
            }

            final Long sequenceToAssign = Long.parseLong(poppedSequenceStr);
            log.info("다음 배정될 담당자 순번: {}", sequenceToAssign);

            DeliveryManager manager = managerRepository
                .findBySequenceAndTypeAndIsActiveFalse(
                    sequenceToAssign,
                    DeliveryManagerType.HUB_MANAGER,
                    false
                )
                .orElseThrow(() -> {
                    log.error("Redis와 DB 불일치. seq = {}", sequenceToAssign);
                    return new AllManagerBusyException("담당자 상태 불일치. 재시도");
                });

            deliveryRouteService.assignManager(route, manager);

            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() { // 커밋까지 됐을 때
                        log.info("배송담당자 배정 성공 routeId = {}, managerId = {}",
                            route.getId(), manager.getId());
                    }

                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_ROLLED_BACK) { // 롤백시에 sequence 복구
                            redisTemplate.opsForList()
                                .rightPush(managerQueueKey, String.valueOf(sequenceToAssign));
                            log.info("트랜잭션 롤백으로 순번 복구 sequence = {}", sequenceToAssign);
                        }
                    }
                }
            );

        } catch (AllManagerBusyException e) {
            log.warn("담당자 없음. routeId = {}", event.getRouteId());
            throw e;

        } catch (Exception e) {
            log.error("오류 발생", e);
            throw e;
        }
    }
}
