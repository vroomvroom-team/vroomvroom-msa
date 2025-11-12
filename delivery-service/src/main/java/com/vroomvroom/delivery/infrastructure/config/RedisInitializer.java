package com.vroomvroom.delivery.infrastructure.config;

import com.vroomvroom.delivery.domain.port.HubClient;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisInitializer implements ApplicationRunner {

    private final StringRedisTemplate redisTemplate;
    private final HubClient hubClient;

    @Override
    public void run(ApplicationArguments args) {
        List<UUID> allHubIds = hubClient.getAllHubId();
        for (UUID hubId : allHubIds) {
            initCompanyManagerQueue(hubId);
        }
        initHubManagerQueue();
    }

    private void initQueue(String key, String initLockKey, String initLockValue) {
        // 분산락 획득 시도 - 확장성을 위해 분산락 사용
        Boolean hasLock = redisTemplate.opsForValue()
            .setIfAbsent(initLockKey, initLockValue, Duration.ofMinutes(1));

        if (Boolean.FALSE.equals(hasLock)) {  // 이미 다른 서버가 락 획득 했으면
            log.info("[{}] 락 획득 실패. 다른 서버가 초기화", initLockKey);
            return;
        }

        try {
            Long size = redisTemplate.opsForList().size(key);
            if (size != null && size > 0) {
                log.info("[{}] 대기열 이미 존재(length = {}). 초기화 필요 X", key, size);
                return;
            }

            List<String> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(String.valueOf(i));
            }
            redisTemplate.delete(key);
            redisTemplate.opsForList().rightPushAll(key, list);

            log.info("[{}] 대기열 초기화 완료", key);
        } finally {
            redisTemplate.delete(initLockKey);
            log.info("[{}] 락 해제", initLockKey);
        }
    }

    private void initCompanyManagerQueue(UUID hubId) {
        String companyManagerKey =
            "cmq:" + hubId + ":"
                + DeliveryManagerType.COMPANY_MANAGER.name(); // 각 허브의 COMPANY_MANAGER 큐 key
        initQueue(companyManagerKey, "hub:company_manager:init_lock",
            "company_manager_initializing");
    }

    private void initHubManagerQueue() {
        String hubManagerKey =
            "hmq:" + DeliveryManagerType.HUB_MANAGER.name(); // 각 허브의 HUB_MANAGER 큐 key
        initQueue(hubManagerKey, "global:hub_manager:init_lock", "hub_manager_initializing");
    }
}
