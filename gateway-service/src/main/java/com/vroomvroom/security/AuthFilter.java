package com.vroomvroom.security;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.vroomvroom.util.AuthConst;
import com.vroomvroom.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	private final JwtUtil jwtUtil;

	public AuthFilter(JwtUtil jwtUtil) {
		super(AuthFilter.Config.class);
		this.jwtUtil = jwtUtil;
	}

	public static class Config {}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			String token = jwtUtil.resolveToken(request);

			if (token == null || !jwtUtil.tokenValidation(token) || jwtUtil.isExpired(token)) {
				return onError(exchange);
			}

			String email = jwtUtil.getEmailFromToken(token);
			String role = jwtUtil.getRoleFromToken(token);
			Long userId = jwtUtil.getUserIdFromToken(token);

			ServerHttpRequest mutated = request.mutate()
				.header(AuthConst.HEADER_USER_ID, String.valueOf(userId))
				.header(AuthConst.HEADER_USER_EMAIL, email)
				.header(AuthConst.HEADER_USER_ROLE, role)
				.build();

			return chain.filter(exchange.mutate().request(mutated).build());
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		String body = String.format(
			"{\"status\": %d, \"message\": \"%s\"}",
			HttpStatus.UNAUTHORIZED.value(),
			"Access 토큰이 유효하지 않습니다."
		);

		DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
		return response.writeWith(Mono.just(buffer))
			.doOnError(error -> DataBufferUtils.release(buffer));
	}
}