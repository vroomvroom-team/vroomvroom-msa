package com.vroomvroom.slackservice.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class SlackExceptionHandler {
	@ExceptionHandler(SlackException.class)
	public ResponseEntity<Map<String, Object>> handleSlackException(SlackException e) {
		SlackErrorCode code = e.getErrorCode();
		log.error("[SlackError] {} - {}", code.getCode(), code.getMessage());

		return ResponseEntity.status(code.getStatus()).body(Map.of(
			"timestamp", LocalDateTime.now(),
			"status", code.getStatus().value(),
			"errorCode", code.getCode(),
			"message", code.getMessage()
		));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
		log.error("[SlackError] Unexpected Exception: ", e);
		return ResponseEntity.internalServerError().body(Map.of(
			"timestamp", LocalDateTime.now(),
			"status", 500,
			"errorCode", "S999",
			"message", "예상치 못한 서버 오류가 발생했습니다."
		));
	}
}
