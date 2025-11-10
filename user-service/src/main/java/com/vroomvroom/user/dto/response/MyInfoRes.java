package com.vroomvroom.user.dto.response;

import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.domain.model.Role;
import com.vroomvroom.user.domain.model.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyInfoRes {

	private Long id;
	private String name;
	private String email;
	private Role role;
	private UserStatus status;
	private String slackId;
	private String companyId;

	public static MyInfoRes from(User user) {
		return MyInfoRes.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.role(user.getRole())
			.status(user.getStatus())
			.slackId(user.getSlackId() != null ? user.getSlackId().getValue() : null)
			.companyId(user.getCompanyId() != null ? user.getCompanyId().getValue() : null)
			.build();
	}
}