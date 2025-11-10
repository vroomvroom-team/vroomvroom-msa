package com.vroomvroom.slackservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SlackErrorCode {
	SLACK_WEBHOOK_MISSING(HttpStatus.BAD_REQUEST, "S001", "Slack Webhook URL이 설정되어 있지 않습니다."),
	SLACK_MESSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S002", "Slack 메시지 전송에 실패했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	SlackErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
