package com.vroomvroom.common.api;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vroomvroom.common.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private final boolean success;
	private final String code;
	private final String message;
	private final T data;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private final LocalDateTime timestamp;

	private ApiResponse(boolean success, String code, String message, T data) {
		this.success = success;
		this.code = code;
		this.message = message;
		this.data = data;
		this.timestamp = LocalDateTime.now();
	}

	@JsonCreator
	private ApiResponse(
			@JsonProperty("success") boolean success,
			@JsonProperty("code") String code,
			@JsonProperty("message") String message,
			@JsonProperty("data") T data,
			@JsonProperty("timestamp") LocalDateTime timestamp
	) {
		this.success = success;
		this.code = code;
		this.message = message;
		this.data = data;
		this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, "OK", "성공", data);
	}

	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, "OK", message, data);
	}

	public static ApiResponse<Void> fail(ErrorCode errorCode) {
		return new ApiResponse<>(false, errorCode.getHttpStatus().name(), errorCode.getMessage(), null);
	}

	public static ApiResponse<Void> fail(String message, ErrorCode errorCode) {
		return new ApiResponse<>(false, errorCode.getHttpStatus().name(), message, null);
	}

	public static ApiResponse<Void> fail(String code, String message) {
		return new ApiResponse<>(false, code, message, null);
	}
}
