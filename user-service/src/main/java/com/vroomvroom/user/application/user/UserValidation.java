package com.vroomvroom.user.application.user;

import org.springframework.stereotype.Component;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.domain.model.Role;
import com.vroomvroom.user.exception.UserErrorCode;

@Component
public class UserValidation {

	public void validateAdminRole(User approver) {
		if (!isAdmin(approver)) {
			throw new CustomException(UserErrorCode.INVALID_ROLE);
		}
	}

	private boolean isAdmin(User user) {
		return Role.MASTER.equals(user.getRole()) || Role.HUB_MANAGER.equals(user.getRole());
	}
}