package com.vroomvroom.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproveUserReq {

	@NotNull(message = "승인 대상 사용자 ID는 필수입니다.")
	private Long userId;

	private boolean approved;
}