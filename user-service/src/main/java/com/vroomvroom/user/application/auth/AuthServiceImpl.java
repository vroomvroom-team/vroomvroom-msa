package com.vroomvroom.user.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vroomvroom.user.application.token.TokenService;
import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.dto.request.LoginReq;
import com.vroomvroom.user.dto.request.SignupReq;
import com.vroomvroom.user.infrastructure.repository.UserRepositoryImpl;
import com.vroomvroom.user.util.UserConst;

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
		User user = authValidation.validateLogin(req);
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
			return header.substring(7);
		}
		return header;
	}
}