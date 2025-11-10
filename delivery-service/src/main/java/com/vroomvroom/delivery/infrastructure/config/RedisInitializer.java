package com.vroomvroom.delivery.infrastructure.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisInitializer implements ApplicationRunner {

    private final StringRedisTemplate redisTemplate;

    private final String INIT_LOCK_KEY;
    private final String MANAGER_QUEUE_KEY;

    public RedisInitializer(
        StringRedisTemplate redisTemplate,
        @Value("${app.redis.keys.init-lock}") String initLockKey,
        @Value("${app.redis.keys.manager-queue}") String managerQueue
    ) {
        this.redisTemplate = redisTemplate;
        this.INIT_LOCK_KEY = initLockKey;
        this.MANAGER_QUEUE_KEY = managerQueue;
    }

    @Override
    public void run(ApplicationArguments args) {
        // 분산락 획득 시도 - 확장성을 위해 분산락 사용
        Boolean hasLock = redisTemplate.opsForValue()
            .setIfAbsent(INIT_LOCK_KEY, "Initializing", Duration.ofMinutes(1));

        if (Boolean.FALSE.equals(hasLock)) { // 이미 다른 서버가 락 획득 했으면
            log.info("락 획득 실패. 다른 서버가 초기화");
            return;
        }

        try {
            Long currentQueueSize = redisTemplate.opsForList().size(MANAGER_QUEUE_KEY);
            if (currentQueueSize != null && currentQueueSize > 0) {
                log.info("대기열 이미 존재(length = {}). 초기화 필요 X", currentQueueSize);
                return;
            }

            log.info("대기열 초기화 시작");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(String.valueOf(i));
            }
            redisTemplate.delete(MANAGER_QUEUE_KEY);
            redisTemplate.opsForList().rightPushAll(MANAGER_QUEUE_KEY, list);

            log.info("대기열 초기화 완료");

        } finally {
            redisTemplate.delete(INIT_LOCK_KEY);
            log.info("락 해제");
        }
    }
}
