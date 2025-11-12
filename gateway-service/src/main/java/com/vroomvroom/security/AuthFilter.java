package com.vroomvroom.security;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.vroomvroom.util.AuthConst;
import com.vroomvroom.util.JwtUtil;
import com.vroomvroom.util.TokenValidationUtil;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	private final JwtUtil jwtUtil;
	private final TokenValidationUtil tokenValidationUtil;

	public AuthFilter(JwtUtil jwtUtil, TokenValidationUtil tokenValidationUtil) {
		super(Config.class);
		this.jwtUtil = jwtUtil;
		this.tokenValidationUtil = tokenValidationUtil;
	}

	public static class Config {}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			String token = jwtUtil.resolveToken(request);

			if (token == null) {
				return onError(exchange, "Access 토큰이 존재하지 않습니다.");
			}

			if (!jwtUtil.tokenValidation(token)) {
				return onError(exchange, "Access 토큰이 변조되었거나 유효하지 않습니다.");
			}

			if (jwtUtil.isExpired(token)) {
				return onError(exchange, "Access 토큰이 만료되었습니다.");
			}

			if (tokenValidationUtil.isBlacklisted(token)) {
				return onError(exchange, "로그아웃된 토큰입니다.");
			}

			Long userId = jwtUtil.getUserIdFromToken(token);
			String email = jwtUtil.getEmailFromToken(token);
			String role = jwtUtil.getRoleFromToken(token);

			ServerHttpRequest mutated = request.mutate()
				.header(AuthConst.HEADER_USER_ID, String.valueOf(userId))
				.header(AuthConst.HEADER_USER_EMAIL, email)
				.header(AuthConst.HEADER_USER_ROLE, role)
				.build();

			return chain.filter(exchange.mutate().request(mutated).build());
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String message) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		String body = String.format(
			"{\"status\": %d, \"message\": \"%s\"}",
			HttpStatus.UNAUTHORIZED.value(),
			message
		);

		DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
		return response.writeWith(Mono.just(buffer));
	}
}