package com.vroomvroom.user.application.token;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.exception.UserErrorCode;
import com.vroomvroom.user.infrastructure.repository.UserRepositoryImpl;
import com.vroomvroom.user.security.JwtProperties;
import com.vroomvroom.user.security.JwtTokenProvider;
import com.vroomvroom.user.util.HeaderUtil;
import com.vroomvroom.user.util.UserConst;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtProperties jwtProperties;
	private final UserRepositoryImpl userRepository;

	public void issueTokens(HttpServletResponse response, User user) {
		String accessToken = createAccessToken(user);
		String refreshToken = createRefreshToken(user);

		storeRefreshToken(user.getEmail(), refreshToken);
		HeaderUtil.setAuthHeaders(response, accessToken, refreshToken);
	}

	public void rotateTokens(HttpServletResponse response, String refreshToken) {
		String email = jwtTokenProvider.getEmail(refreshToken);
		validateStoredRefreshToken(email, refreshToken);

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

		String newAccessToken = createAccessToken(user);
		String newRefreshToken = createRefreshToken(user);

		storeRefreshToken(email, newRefreshToken);
		HeaderUtil.setAuthHeaders(response, newAccessToken, newRefreshToken);
	}

	public void logout(HttpServletResponse response, String accessToken) {
		String email = jwtTokenProvider.getEmail(accessToken);
		long ttl = jwtTokenProvider.getRemainingExpiration(accessToken);

		addToBlacklist(email, accessToken, ttl);
		redisTemplate.delete(email);

		HeaderUtil.clearAuthHeaders(response);
	}

	private String createAccessToken(User user) {
		return jwtTokenProvider.createAccessToken(user, jwtProperties.getAccessTokenValidity());
	}

	private String createRefreshToken(User user) {
		return jwtTokenProvider.createRefreshToken(user, jwtProperties.getRefreshTokenValidity());
	}

	private void storeRefreshToken(String email, String refreshToken) {
		redisTemplate.opsForValue().set(
			email,
			refreshToken,
			jwtProperties.getRefreshTokenValidity(),
			TimeUnit.MILLISECONDS
		);
	}

	private void validateStoredRefreshToken(String email, String refreshToken) {
		String storedToken = redisTemplate.opsForValue().get(email);
		if (storedToken == null || !storedToken.equals(refreshToken)) {
			throw new CustomException(UserErrorCode.INVALID_STATUS);
		}
	}

	private void addToBlacklist(String email, String accessToken, long ttl) {
		String blacklistKey = String.format("%s%s:%s",
			UserConst.REDIS_BLACKLIST_PREFIX, email, accessToken);

		redisTemplate.opsForValue().set(
			blacklistKey,
			"true",
			ttl,
			TimeUnit.MILLISECONDS
		);
	}
}