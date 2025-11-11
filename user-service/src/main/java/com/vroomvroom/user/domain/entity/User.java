package com.vroomvroom.user.domain.entity;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.user.domain.model.Role;
import com.vroomvroom.user.domain.model.UserStatus;
import com.vroomvroom.user.domain.vo.CompanyId;
import com.vroomvroom.user.domain.vo.SlackId;
import com.vroomvroom.user.dto.request.SignupReq;
import com.vroomvroom.user.exception.UserErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_user")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "username")
	private Long id;

	private String name;

	private String email;

	private String password;

	@Embedded
	private SlackId slackId;

	@Embedded
	private CompanyId companyId;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column(name = "is_public")
	private boolean isPublic;

	private String createdBy;

	private String updatedBy;

	private String deletedBy;

	public static User pending(SignupReq req, String encodedPassword) {
		return User.builder()
			.name(req.getName())
			.email(req.getUserEmail())
			.password(encodedPassword)
			.slackId(req.toSlackId())
			.companyId(req.toCompanyId())
			.status(UserStatus.PENDING)
			.role(Role.DELIVERY_MANAGER)
			.isPublic(false)
			.build();
	}

	public void approve(User approver) {
		validateApproverRole(approver);
		this.status = UserStatus.APPROVED;
		this.updatedBy = approver.getName();
	}

	public void reject(User approver) {
		validateApproverRole(approver);
		this.status = UserStatus.REJECTED;
		this.updatedBy = approver.getName();
	}

	public void assignRole(Role newRole, User approver) {
		validateApproverRole(approver);
		this.role = newRole;
		this.updatedBy = approver.getName();
	}

	public void deactivate(String adminName) {
		this.deletedBy = adminName;
		markAsDeleted();
	}

	private void validateApproverRole(User approver) {
		Role role = approver.getRole();

		if (role != Role.MASTER && role != Role.HUB_MANAGER) {
			throw new CustomException(UserErrorCode.INVALID_ROLE);
		}
	}
}