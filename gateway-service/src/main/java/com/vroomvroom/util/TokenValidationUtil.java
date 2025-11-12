package com.vroomvroom.util;

import java.util.Objects;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenValidationUtil {

	private final StringRedisTemplate redisTemplate;

	public TokenValidationUtil(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public boolean isBlacklisted(String token) {
		return Boolean.TRUE.equals(Objects.requireNonNull(redisTemplate.keys("blacklist*:" + token))
			.stream().findAny().isPresent());
	}
}