package com.vroomvroom.delivery.application.service;

import com.vroomvroom.delivery.domain.event.ManagerAssignmentEvent;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import com.vroomvroom.delivery.infrastructure.external.KafkaAssignmentMessageSender;
import com.vroomvroom.delivery.infrastructure.repository.UnassignedRouteIds;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PendingAssignmentReplayer {

    private final DeliveryRepository deliveryRepository;
    private final KafkaAssignmentMessageSender sender;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.redis.keys.replay-lock}")
    String replayLockKey;

    // 30초마다 미배정된 배송경로 일부 재시도
    @Scheduled(fixedDelayString = "${app.assignment.replay-interval-ms:30000}")
    @Transactional(readOnly = true)
    public void replayUnassigned() {

        // 확장성을 위해 분산락으로 처리
        Boolean hasLock = redisTemplate.opsForValue()
            .setIfAbsent(replayLockKey, "replaying", Duration.ofSeconds(10));

        if (Boolean.FALSE.equals(hasLock)) { // 이미 다른 서버가 락 획득 했으면
            log.info("락 획득 실패. 다른 서버가 재시도");
            return;
        }

        try {
            Page<UnassignedRouteIds> page = deliveryRepository
                .findUnassignedRouteKeys(PageRequest.of(0, 10));

            if (page.isEmpty()) {
                log.info("미배정된 경로 없음");
                return;
            }

            for (UnassignedRouteIds unassignedRouteIds : page) {
                UUID routeId = unassignedRouteIds.getRouteId();
                UUID deliveryId = unassignedRouteIds.getDeliveryId();
                sender.send(deliveryId, new ManagerAssignmentEvent(routeId));
            }
            log.info("미배정된 경로 {}개 재시도 발행 완료", page.getNumberOfElements());

        } catch (Exception e) {
            log.error("미배정된 경로 재시도 중 오류 발생", e);
        } finally {
            redisTemplate.delete(replayLockKey);
        }
    }
}
