package com.vroomvroom.delivery.infrastructure;

import com.vroomvroom.delivery.domain.port.AssignmentQueuePort;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisAssignmentQueueAdapter implements AssignmentQueuePort {

    private final StringRedisTemplate redisTemplate;

    @Override
    public Optional<Long> popCompanyManagerSequence(UUID hubId) {
        String value = redisTemplate.opsForList().leftPop(cmqKey(hubId));
        return value == null ? Optional.empty() : Optional.of(Long.parseLong(value));
    }

    @Override
    public void pushCompanyManagerSequence(UUID hubId, Long sequence) {
        redisTemplate.opsForList().rightPush(cmqKey(hubId), String.valueOf(sequence));
    }

    @Override
    public Optional<Long> popHubManagerSequence(String key) {
        String value = redisTemplate.opsForList().leftPop(key);
        return value == null ? Optional.empty() : Optional.of(Long.parseLong(value));
    }

    @Override
    public void pushHubManagerSequence(String key, Long sequence) {
        redisTemplate.opsForList().rightPush(key, String.valueOf(sequence));
    }


    private String cmqKey(UUID hubId) {
        return "cmq:" + hubId + ":"
            + DeliveryManagerType.COMPANY_MANAGER.name();
    }
}
