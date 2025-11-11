package com.vroomvroom.user.util;

public final class UserConst {

	private UserConst() {}

	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_REFRESH_TOKEN = "X-Refresh-Token";
	public static final String HEADER_USER_ID = "X-User-Id";

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String REDIS_BLACKLIST_PREFIX = "blacklist:";

	public static final String CLAIM_USER_ID = "userId";
	public static final String CLAIM_EMAIL = "email";
	public static final String CLAIM_ROLE = "role";

	public static final String FIELD_CREATED_AT = "createdAt";

	public static final String DEFAULT_PAGE = "0";
	public static final String DEFAULT_SIZE = "10";

}