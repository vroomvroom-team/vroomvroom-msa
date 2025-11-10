package com.vroomvroom.user.exception;

import com.vroomvroom.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {

	INVALID_ROLE(HttpStatus.FORBIDDEN, "승인 권한이 없습니다."),
	INVALID_STATUS(HttpStatus.BAD_REQUEST, "잘못된 사용자 상태입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	UserErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}