package com.vroomvroom.user.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.user.application.auth.AuthService;
import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.dto.request.LoginReq;
import com.vroomvroom.user.dto.request.SignupReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	// 회원가입 요청 (승인 대기 상태)
	@PostMapping("/signup")
	public ApiResponse<User> signup(@RequestBody SignupReq req) {
		User user = authService.signup(req);
		return ApiResponse.success("회원가입 요청이 완료되었습니다. 승인 대기 중입니다.", user);
	}

	// 로그인
	@PostMapping("/login")
	public ApiResponse<Void> login(@RequestBody LoginReq req, HttpServletResponse response) {
		authService.login(req, response);
		return ApiResponse.success("로그인 성공", null);
	}

	// 로그아웃
	@PostMapping("/logout")
	public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		authService.logout(response, request);
		return ApiResponse.success("로그아웃 완료", null);
	}

	// 토큰 재발급
	@PostMapping("/reissue")
	public ApiResponse<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
		authService.reissue(request, response);
		return ApiResponse.success("토큰이 재발급되었습니다.", null);
	}
}