package com.vroomvroom.common.security;

import com.vroomvroom.common.model.Role;

public record UserPrincipal(
	Long id,
	String username,
	Role role
) {

	public boolean hasRole(Role targetRole) {
		return this.role == targetRole;
	}

	public String getRole() {
		return this.role.name();
	}
}