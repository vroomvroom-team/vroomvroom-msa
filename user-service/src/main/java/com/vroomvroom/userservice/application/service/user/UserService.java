package com.vroomvroom.userservice.application.service.user;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.common.model.Role;
import com.vroomvroom.userservice.presentation.dto.response.MyInfoRes;
import com.vroomvroom.userservice.presentation.dto.response.PendingUserRes;

public interface UserService {

	// 사용자 승인 처리
	void approveUser(Long userId);

	// 사용자 가입 거절
	void rejectUser(Long userId);

	// 역할 부여 및 변경
	void assignRole(Long userId, Role newRole);

	// 사용자 비활성화
	void deactivateUser(Long userId);

	// 가입 요청 목록(PENDING 상태) 조회
	PageResponse<PendingUserRes> getPendingUsers(int page, int size);

	// 특정 ID 사용자가 존재하는지 여부 반환
	boolean existsById(Long userId);

	// 특정 역할을 가진 사용자인지 확인
	boolean hasRole(Long userId, Role role);

	// 사용자 정보 확인
	MyInfoRes getMyInfo(Long userId);
}