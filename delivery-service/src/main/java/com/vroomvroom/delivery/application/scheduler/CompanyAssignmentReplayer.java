package com.vroomvroom.delivery.application.scheduler;

import com.vroomvroom.delivery.application.service.DeliveryService;
import com.vroomvroom.delivery.domain.port.HubClient;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import com.vroomvroom.delivery.domain.vo.DeliveryStatus;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyAssignmentReplayer {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;
    private final StringRedisTemplate redisTemplate;
    private final HubClient hubClient;

    @Scheduled(fixedDelayString = "${app.assignment.replay-interval-ms}")
    @Transactional(readOnly = true)
    public void replayForUnassigned() {
        List<UUID> hubIds = hubClient.getAllHubId();
        for (UUID hubId : hubIds) {
            String lockKey = "cmq:replay-lock:" + hubId;

            // 확장성을 위해 분산락으로 처리
            Boolean hasLock = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "replaying", Duration.ofSeconds(30));

            if (Boolean.FALSE.equals(hasLock)) { // 이미 다른 서버가 락 획득 했으면
                log.info("락 획득 실패. 다른 서버가 재시도");
                continue;
            }

            try {
                Page<UUID> candidateIds = deliveryRepository.findIdsByStatus(
                    DeliveryStatus.DELIVERY_PREPARING,
                    hubId,
                    PageRequest.of(0, 10, Sort.by(Direction.ASC, "createdAt"))
                );

                if (candidateIds.isEmpty()) {
                    log.info("[{}] 미배정된 경로 없음", hubId);
                    continue;
                }

                for (UUID candidateId : candidateIds.getContent()) {
                    try {
                        deliveryService.handoffToCompany(candidateId);
                    } catch (Exception e) {
                        log.warn("업체담당자 배정 재시도 실패 deliveryId = {}", candidateId, e);
                    }
                }
            } catch (Exception e) {
                log.error("미배정된 경로 재시도 중 오류 발생", e);
            } finally {
                redisTemplate.delete(lockKey);
            }

        }
    }
}
