package com.vroomvroom.util;

public class AuthConst {

	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_USER_ID = "X-User-Id";
	public static final String HEADER_USER_EMAIL = "X-User-Email";
	public static final String HEADER_USER_ROLE = "X-User-Role";

	public static final String BEARER_PREFIX = "Bearer ";

	public static final String CLAIM_EMAIL = "email";
	public static final String CLAIM_ROLE = "role";
	public static final String CLAIM_USER_ID = "userId";

	private AuthConst() {}
}
