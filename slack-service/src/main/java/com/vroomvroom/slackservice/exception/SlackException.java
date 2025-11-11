package com.vroomvroom.slackservice.exception;

import lombok.Getter;

@Getter
public class SlackException extends RuntimeException {
	private final SlackErrorCode errorCode;

	public SlackException(SlackErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
