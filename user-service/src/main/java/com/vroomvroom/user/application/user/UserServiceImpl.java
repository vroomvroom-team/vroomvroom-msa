package com.vroomvroom.user.application.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.domain.model.Role;
import com.vroomvroom.user.domain.model.UserStatus;
import com.vroomvroom.user.dto.response.MyInfoRes;
import com.vroomvroom.user.dto.response.PendingUserRes;
import com.vroomvroom.user.exception.UserErrorCode;
import com.vroomvroom.user.infrastructure.repository.UserRepositoryImpl;
import com.vroomvroom.user.util.UserConst;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepositoryImpl userRepository;
	private final UserValidation userValidation;

	@Override
	public void approveUser(Long userId, Long approverId) {
		UserPair pair = getUsersOrThrow(userId, approverId);
		userValidation.validateAdminRole(pair.approver);
		pair.target.approve(pair.approver);
	}

	@Override
	public void rejectUser(Long userId, Long approverId) {
		UserPair pair = getUsersOrThrow(userId, approverId);
		userValidation.validateAdminRole(pair.approver);
		pair.target.reject(pair.approver);
	}

	@Override
	public void assignRole(Long userId, Role newRole, Long approverId) {
		UserPair pair = getUsersOrThrow(userId, approverId);
		userValidation.validateAdminRole(pair.approver);
		pair.target.assignRole(newRole, pair.approver);
	}

	@Override
	public void deactivateUser(Long userId, Long approverId) {
		UserPair pair = getUsersOrThrow(userId, approverId);
		userValidation.validateAdminRole(pair.approver);
		pair.target.deactivate(pair.approver.getName());
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
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

		return MyInfoRes.from(user);
	}

	private UserPair getUsersOrThrow(Long userId, Long approverId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

		User approver = userRepository.findById(approverId)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

		return new UserPair(user, approver);
	}

	private record UserPair(User target, User approver) {}
}