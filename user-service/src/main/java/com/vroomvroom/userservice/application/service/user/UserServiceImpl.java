package com.vroomvroom.userservice.application.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.common.security.UserPrincipal;
import com.vroomvroom.userservice.domain.entity.User;
import com.vroomvroom.common.model.Role;
import com.vroomvroom.userservice.domain.model.UserStatus;
import com.vroomvroom.userservice.presentation.dto.response.MyInfoRes;
import com.vroomvroom.userservice.presentation.dto.response.PendingUserRes;
import com.vroomvroom.userservice.exception.UserErrorCode;
import com.vroomvroom.userservice.infrastructure.repository.UserRepositoryImpl;
import com.vroomvroom.userservice.application.util.UserConst;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepositoryImpl userRepository;

	@Override
	public void approveUser(Long userId) {
		User approver = getCurrentUser();
		User target = getUserOrThrow(userId);
		target.approve(approver);
	}

	@Override
	public void rejectUser(Long userId) {
		User approver = getCurrentUser();
		User target = getUserOrThrow(userId);
		target.reject(approver);
	}

	@Override
	public void assignRole(Long userId, Role newRole) {
		User approver = getCurrentUser();
		User target = getUserOrThrow(userId);
		target.assignRole(newRole, approver);
	}

	@Override
	public void deactivateUser(Long userId) {
		User approver = getCurrentUser();
		User target = getUserOrThrow(userId);
		target.deactivate(approver.getName());
	}

	@Override
	public PageResponse<PendingUserRes> getPendingUsers(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UserConst.FIELD_CREATED_AT));

		Page<User> pendingUsers = userRepository.findAllByStatus(UserStatus.PENDING, pageable);
		Page<PendingUserRes> dtoPage = pendingUsers.map(PendingUserRes::from);
		return PageResponse.fromPage(dtoPage);
	}

	@Override
	public boolean existsById(Long userId) {
		return userRepository.existsById(userId);
	}

	@Override
	public boolean hasRole(Long userId, Role role) {
		return userRepository.findById(userId)
			.map(user -> user.getRole() == role)
			.orElse(false);
	}

	@Override
	public MyInfoRes getMyInfo(Long userId) {
		User user = getUserOrThrow(userId);
		return MyInfoRes.from(user);
	}

	private User getUserOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
	}

	private User getCurrentUser() {
		UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();

		return userRepository.findById(principal.id())
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
	}
}