package com.vroomvroom.user.dto.response;

import com.vroomvroom.user.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PendingUserRes {

	private final Long id;
	private final String name;
	private final String email;
	private final String slackId;
	private final String companyId;
	private final LocalDateTime createdAt;

	public static PendingUserRes from(User user) {
		return new PendingUserRes(
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.getSlackId().getValue(),
			user.getCompanyId().getValue(),
			user.getCreatedAt()
		);
	}
}