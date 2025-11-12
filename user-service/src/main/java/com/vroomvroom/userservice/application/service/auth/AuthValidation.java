package com.vroomvroom.userservice.application.service.auth;

import org.springframework.stereotype.Component;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.userservice.application.util.UserConst;
import com.vroomvroom.userservice.domain.entity.User;
import com.vroomvroom.userservice.exception.UserErrorCode;
import com.vroomvroom.userservice.infrastructure.repository.UserRepositoryImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthValidation {

	private final UserRepositoryImpl userRepository;

	public void validateDuplicateEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new CustomException(UserErrorCode.DUPLICATE_USER);
		}
	}

	public User validateLogin(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
	}

	public String validateRefreshToken(HttpServletRequest request) {
		String refreshToken = request.getHeader(UserConst.HEADER_REFRESH_TOKEN);
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new CustomException(UserErrorCode.INVALID_STATUS);
		}

		return refreshToken;
	}
}