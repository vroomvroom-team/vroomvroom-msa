package com.vroomvroom.user.application.user;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.user.domain.model.Role;
import com.vroomvroom.user.dto.response.MyInfoRes;
import com.vroomvroom.user.dto.response.PendingUserRes;

public interface UserService {

	// 사용자 승인 처리 (MASTER, HUB_MANAGER 만 가능)
	void approveUser(Long userId, Long approverId);

	// 사용자 가입 거절 (MASTER, HUB_MANAGER 만 가능)
	void rejectUser(Long userId, Long approverId);

	// 역할 부여 및 변경 (MASTER, HUB_MANAGER 만 가능)
	void assignRole(Long userId, Role newRole, Long approverId);

	// 사용자 비활성화 (관리자 권한 필요)
	void deactivateUser(Long userId, Long approverId);

	// 가입 요청 목록(PENDING 상태) 조회
	PageResponse<PendingUserRes> getPendingUsers(int page, int size);

	// 특정 ID 사용자가 존재하는지 여부 반환
	boolean existsById(Long userId);

	// 특정 역할을 가진 사용자인지 확인
	boolean hasRole(Long userId, Role role);

	// 사용자 정보 확인
	MyInfoRes getMyInfo(Long userId);
}