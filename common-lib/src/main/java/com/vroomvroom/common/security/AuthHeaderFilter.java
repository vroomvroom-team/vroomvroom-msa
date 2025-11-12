package com.vroomvroom.common.security;

import com.vroomvroom.common.model.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class AuthHeaderFilter extends OncePerRequestFilter {

	private static final String HEADER_USER_ID = "X-User-Id";
	private static final String HEADER_USER_EMAIL = "X-User-Email";
	private static final String HEADER_USER_ROLE = "X-User-Role";

	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

		String userIdHeader = request.getHeader(HEADER_USER_ID);
		String email = request.getHeader(HEADER_USER_EMAIL);
		String roleHeader = request.getHeader(HEADER_USER_ROLE);

		if (userIdHeader != null && roleHeader != null) {
			try {
				Long userId = Long.parseLong(userIdHeader);
				Role role = Role.valueOf(roleHeader);
				List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));

				UserPrincipal principal = new UserPrincipal(userId, email, role);
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(principal, null, authorities);

				SecurityContextHolder.getContext().setAuthentication(authentication);

				log.debug("AuthHeaderFilter: userId={}, email={}, role={}", userId, email, role);
			} catch (IllegalArgumentException e) {
				log.warn("AuthHeaderFilter: 잘못된 헤더 값 — userId={}, role={}", userIdHeader, roleHeader, e);
			}
		}

		filterChain.doFilter(request, response);
	}
}