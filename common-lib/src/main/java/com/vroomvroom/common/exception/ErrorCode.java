package com.vroomvroom.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

	HttpStatus getHttpStatus();
	String getMessage();

}