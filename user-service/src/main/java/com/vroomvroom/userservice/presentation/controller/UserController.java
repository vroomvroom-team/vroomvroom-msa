package com.vroomvroom.userservice.presentation.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.common.model.Role;
import com.vroomvroom.common.security.UserPrincipal;
import com.vroomvroom.userservice.application.service.user.UserService;
import com.vroomvroom.userservice.presentation.dto.response.MyInfoRes;
import com.vroomvroom.userservice.presentation.dto.response.PendingUserRes;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 가입 대기중 사용자 목록 조회
	@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
	@GetMapping("/pending")
	public ApiResponse<PageResponse<PendingUserRes>> getPendingUsers(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ApiResponse.success(userService.getPendingUsers(page, size));
	}

	// 사용자 승인
	@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
	@PostMapping("/{userId}/approve")
	public ApiResponse<Void> approveUser(@PathVariable Long userId) {
		userService.approveUser(userId);
		return ApiResponse.success("사용자 승인 완료", null);
	}

	// 사용자 가입 거절
	@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
	@PostMapping("/{userId}/reject")
	public ApiResponse<Void> rejectUser(@PathVariable Long userId) {
		userService.rejectUser(userId);
		return ApiResponse.success("사용자 가입 거절 완료", null);
	}

	// 역할 부여 및 변경
	@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
	@PatchMapping("/{userId}/role")
	public ApiResponse<Void> assignRole(
		@PathVariable Long userId,
		@RequestParam Role newRole
	) {
		userService.assignRole(userId, newRole);
		return ApiResponse.success("역할 변경 완료", null);
	}

	// 사용자 비활성화
	@PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
	@DeleteMapping("/{userId}")
	public ApiResponse<Void> deactivateUser(@PathVariable Long userId) {
		userService.deactivateUser(userId);
		return ApiResponse.success("사용자 비활성화 완료", null);
	}

	// 특정 사용자 존재 여부 (모든 인증 사용자 가능)
	@GetMapping("/{userId}/exists")
	public ApiResponse<Boolean> existsById(@PathVariable Long userId) {
		return ApiResponse.success(userService.existsById(userId));
	}

	// 특정 사용자 역할 확인 (모든 인증 사용자 가능)
	@GetMapping("/{userId}/role")
	public ApiResponse<Boolean> hasRole(@PathVariable Long userId, @RequestParam Role role) {
		return ApiResponse.success(userService.hasRole(userId, role));
	}

	// 내 정보 조회 (모든 인증 사용자 가능)
	@GetMapping("/me")
	public ApiResponse<MyInfoRes> getMyInfo(@AuthenticationPrincipal UserPrincipal principal) {
		return ApiResponse.success(userService.getMyInfo(principal.id()));
	}
}