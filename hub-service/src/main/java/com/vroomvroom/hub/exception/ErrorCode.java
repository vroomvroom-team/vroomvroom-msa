package com.vroomvroom.hub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 경로를 찾을 수 없습니다."),
    NOT_RESOURCE_OWNER(HttpStatus.FORBIDDEN, "해당 리소스 소유자가 아닙니다."),

    DUPLICATE_HUB_NAME(HttpStatus.CONFLICT, "해당 허브는 이미 존재하는 허브입니다."),
    INVALID_LOCATION(HttpStatus.BAD_REQUEST, "유효하지 않은 위·경도입니다. (위도 범위: -90 ~ 90, 경도 범위: -180 ~ 180)"),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}