package com.vroomvroom.userservice.application.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.userservice.application.service.token.TokenService;
import com.vroomvroom.userservice.domain.entity.User;
import com.vroomvroom.userservice.domain.model.UserStatus;
import com.vroomvroom.userservice.exception.UserErrorCode;
import com.vroomvroom.userservice.presentation.dto.request.LoginReq;
import com.vroomvroom.userservice.presentation.dto.request.SignupReq;
import com.vroomvroom.userservice.infrastructure.repository.UserRepositoryImpl;
import com.vroomvroom.userservice.application.util.UserConst;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

	private final UserRepositoryImpl userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;
	private final AuthValidation authValidation;

	@Override
	public User signup(SignupReq req) {
		authValidation.validateDuplicateEmail(req.getUserEmail());
		String encodedPassword = passwordEncoder.encode(req.getPassword());
		return userRepository.save(User.pending(req, encodedPassword));
	}

	@Override
	public void login(LoginReq req, HttpServletResponse response) {
		User user = authValidation.validateLogin(req.getEmail());

		if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
			throw new CustomException(UserErrorCode.INVALID_PASSWORD);
		}

		if (user.getStatus() != UserStatus.APPROVED) {
			throw new CustomException(UserErrorCode.INVALID_ROLE);
		}

		tokenService.issueTokens(response, user);
	}

	@Override
	public void logout(HttpServletResponse response, HttpServletRequest request) {
		String accessToken = extractToken(request.getHeader("Authorization"));
		tokenService.logout(response, accessToken);
	}

	@Override
	public void reissue(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = authValidation.validateRefreshToken(request);
		tokenService.rotateTokens(response, refreshToken);
	}

	private String extractToken(String header) {
		if (header != null && header.startsWith(UserConst.TOKEN_PREFIX)) {
			return header.substring(UserConst.TOKEN_PREFIX.length());
		}
		return header;
	}
}