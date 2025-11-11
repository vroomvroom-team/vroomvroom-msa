package com.vroomvroom.user.application.auth;

import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.dto.request.LoginReq;
import com.vroomvroom.user.dto.request.SignupReq;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

	// 회원가입 요청 (승인 대기 상태로 저장)
	User signup(SignupReq req);

	// 로그인 - 승인된 사용자만 가능, JWT 발급
	void login(LoginReq req, HttpServletResponse response);

	// 로그아웃 - 토큰 삭제 / 블랙리스트 등록
	void logout(HttpServletResponse response, HttpServletRequest request);

	// 토큰 재발급 (Access, Refresh 모두 재생성)
	void reissue(HttpServletRequest request, HttpServletResponse response);
}