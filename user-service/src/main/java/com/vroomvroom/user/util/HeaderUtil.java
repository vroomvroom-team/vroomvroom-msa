package com.vroomvroom.user.util;

import jakarta.servlet.http.HttpServletResponse;

public class HeaderUtil {

	private HeaderUtil() {}

	public static void setAuthHeaders(HttpServletResponse response, String accessToken, String refreshToken) {
		response.setHeader(UserConst.HEADER_AUTHORIZATION, UserConst.TOKEN_PREFIX + accessToken);
		response.setHeader(UserConst.HEADER_REFRESH_TOKEN, refreshToken);
	}

	public static void clearAuthHeaders(HttpServletResponse response) {
		response.setHeader(UserConst.HEADER_AUTHORIZATION, "");
		response.setHeader(UserConst.HEADER_REFRESH_TOKEN, "");
	}
}