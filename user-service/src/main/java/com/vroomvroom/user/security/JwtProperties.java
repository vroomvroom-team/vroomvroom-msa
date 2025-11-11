package com.vroomvroom.user.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class JwtProperties {

	@Value("${spring.jwt.access-expiration}")
	private long accessTokenValidity;

	@Value("${spring.jwt.refresh-expiration}")
	private long refreshTokenValidity;

}