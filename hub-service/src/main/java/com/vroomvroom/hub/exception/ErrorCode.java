package com.vroomvroom.hub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Hub
    DUPLICATE_HUB_NAME(HttpStatus.CONFLICT, "해당 허브는 이미 존재하는 허브입니다."),
    INVALID_LOCATION(HttpStatus.BAD_REQUEST, "유효하지 않은 위·경도입니다. (위도 범위: -90 ~ 90, 경도 범위: -180 ~ 180)"),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}