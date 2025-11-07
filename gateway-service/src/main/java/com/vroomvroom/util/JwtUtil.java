package com.vroomvroom.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	private final String secret;
	private JwtParser jwtParser;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
		this.secret = secret;
	}

	@PostConstruct
	public void init() {
		SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
	}

	public boolean tokenValidation(String token) {
		try {
			jwtParser.parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public boolean isExpired(String token) {
		try {
			return getAllClaims(token).getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	public String getEmailFromToken(String token) {
		return getAllClaims(token).get(AuthConst.CLAIM_EMAIL, String.class);
	}

	public Long getUserIdFromToken(String token) {
		Number id = getAllClaims(token).get(AuthConst.CLAIM_USER_ID, Number.class);
		return id != null ? id.longValue() : null;
	}

	public String getRoleFromToken(String token) {
		return getAllClaims(token).get(AuthConst.CLAIM_ROLE, String.class);
	}

	private Claims getAllClaims(String token) {
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public String resolveToken(ServerHttpRequest request) {
		String header = request.getHeaders().getFirst(AuthConst.HEADER_AUTHORIZATION);
		if (header != null && header.startsWith(AuthConst.BEARER_PREFIX)) {
			return header.substring(AuthConst.BEARER_PREFIX.length());
		}
		return null;
	}
}