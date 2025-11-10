package com.vroomvroom.user.dto.request;

import com.vroomvroom.user.domain.vo.CompanyId;
import com.vroomvroom.user.domain.vo.SlackId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupReq {

	@NotBlank(message = "이름은 필수입니다.")
	private String name;

	@Pattern(
		regexp = "^[a-z0-9]{4,10}$",
		message = "아이디는 4~10자의 소문자 알파벳과 숫자만 사용할 수 있습니다."
	)
	private String userEmail;

	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,15}$",
		message = "비밀번호는 8~15자이며, 영문 대소문자, 숫자, 특수문자를 포함해야 합니다."
	)
	private String password;

	@NotBlank(message = "Slack ID는 필수입니다.")
	private String slackId;

	@NotBlank(message = "소속 업체명 또는 허브명은 필수입니다.")
	private String companyId;

	public SlackId toSlackId() {
		return new SlackId(slackId);
	}

	public CompanyId toCompanyId() {
		return new CompanyId(companyId);
	}
}