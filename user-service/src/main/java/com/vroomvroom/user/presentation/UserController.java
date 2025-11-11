package com.vroomvroom.user.presentation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.user.application.user.UserService;
import com.vroomvroom.user.domain.model.Role;
import com.vroomvroom.user.dto.response.MyInfoRes;
import com.vroomvroom.user.dto.response.PendingUserRes;
import com.vroomvroom.user.util.UserConst;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 가입 대기중 사용자 목록 조회
	@GetMapping("/pending")
	public ApiResponse<PageResponse<PendingUserRes>> getPendingUsers(
		@RequestParam(defaultValue = UserConst.DEFAULT_PAGE) int page,
		@RequestParam(defaultValue = UserConst.DEFAULT_SIZE) int size
	) {
		return ApiResponse.success(userService.getPendingUsers(page, size));
	}

	// 사용자 승인
	@PostMapping("/{userId}/approve")
	public ApiResponse<Void> approveUser(
		@PathVariable Long userId,
		@RequestHeader(UserConst.HEADER_USER_ID) Long approverId
	) {
		userService.approveUser(userId, approverId);
		return ApiResponse.success("사용자 승인 완료", null);
	}

	// 사용자 가입 거절
	@PostMapping("/{userId}/reject")
	public ApiResponse<Void> rejectUser(
		@PathVariable Long userId,
		@RequestHeader(UserConst.HEADER_USER_ID) Long approverId
	) {
		userService.rejectUser(userId, approverId);
		return ApiResponse.success("사용자 가입 거절 완료", null);
	}

	// 역할 부여 및 변경
	@PatchMapping("/{userId}/role")
	public ApiResponse<Void> assignRole(
		@PathVariable Long userId,
		@RequestParam Role newRole,
		@RequestHeader(UserConst.HEADER_USER_ID) Long approverId
	) {
		userService.assignRole(userId, newRole, approverId);
		return ApiResponse.success("역할 변경 완료", null);
	}

	// 사용자 비활성화
	@DeleteMapping("/{userId}")
	public ApiResponse<Void> deactivateUser(
		@PathVariable Long userId,
		@RequestHeader(UserConst.HEADER_USER_ID) Long approverId
	) {
		userService.deactivateUser(userId, approverId);
		return ApiResponse.success("사용자 비활성화 완료", null);
	}

	// 특정 사용자 존재 여부
	@GetMapping("/{userId}/exists")
	public ApiResponse<Boolean> existsById(@PathVariable Long userId) {
		return ApiResponse.success(userService.existsById(userId));
	}

	// 특정 사용자 역할 확인
	@GetMapping("/{userId}/role")
	public ApiResponse<Boolean> hasRole(
		@PathVariable Long userId,
		@RequestParam Role role
	) {
		return ApiResponse.success(userService.hasRole(userId, role));
	}

	// 내 정보 조회
	@GetMapping("/me")
	public ApiResponse<MyInfoRes> getMyInfo(
		@RequestHeader(UserConst.HEADER_USER_ID) Long userId
	) {
		return ApiResponse.success(userService.getMyInfo(userId));
	}
}