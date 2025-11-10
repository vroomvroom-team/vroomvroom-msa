package com.vroomvroom.user.security;

import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.util.UserConst;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

	@Value("${spring.jwt.secret}")
	private String secretKey;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	public String createAccessToken(User user, long accessTokenValidity) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + accessTokenValidity);

		return Jwts.builder()
			.setSubject(user.getEmail())
			.claim(UserConst.CLAIM_USER_ID, user.getId())
			.claim(UserConst.CLAIM_EMAIL, user.getEmail())
			.claim(UserConst.CLAIM_ROLE, user.getRole().name())
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(User user, long refreshTokenValidity) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + refreshTokenValidity);

		return Jwts.builder()
			.setSubject(user.getEmail())
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public long getRemainingExpiration(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration()
			.getTime() - System.currentTimeMillis();
	}

	public String getEmail(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get(UserConst.CLAIM_EMAIL, String.class);
	}

	public Long getUserId(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get(UserConst.CLAIM_USER_ID, Long.class);
	}

	public String getRole(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get(UserConst.CLAIM_ROLE, String.class);
	}
}