package com.vroomvroom.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public abstract class SecurityConfigBase {

	@Bean
	public AuthHeaderFilter authHeaderFilter() {
		return new AuthHeaderFilter();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthHeaderFilter authHeaderFilter) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.addFilterBefore(authHeaderFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers(
					"/swagger-ui/**",
					"/actuator/**",
					"/health/**"
				).permitAll();

				configureAuthorization(auth);
				auth.anyRequest().authenticated();
			});

		return http.build();
	}

	protected void configureAuthorization(
		AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
	}
}