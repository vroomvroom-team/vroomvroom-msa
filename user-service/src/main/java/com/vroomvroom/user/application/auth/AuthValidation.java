package com.vroomvroom.user.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.domain.model.UserStatus;
import com.vroomvroom.user.dto.request.LoginReq;
import com.vroomvroom.user.exception.UserErrorCode;
import com.vroomvroom.user.infrastructure.repository.UserRepositoryImpl;
import com.vroomvroom.user.util.UserConst;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthValidation {

	private final UserRepositoryImpl userRepository;
	private final PasswordEncoder passwordEncoder;

	public void validateDuplicateEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new CustomException(UserErrorCode.DUPLICATE_USER);
		}
	}

	public User validateLogin(LoginReq req) {
		User user = userRepository.findByEmail(req.getEmail())
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
			throw new CustomException(UserErrorCode.INVALID_STATUS);
		}

		if (user.getStatus() != UserStatus.APPROVED) {
			throw new CustomException(UserErrorCode.INVALID_ROLE);
		}

		return user;
	}

	public String validateRefreshToken(HttpServletRequest request) {
		String refreshToken = request.getHeader(UserConst.HEADER_REFRESH_TOKEN);
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new CustomException(UserErrorCode.INVALID_STATUS);
		}
		return refreshToken;
	}
}