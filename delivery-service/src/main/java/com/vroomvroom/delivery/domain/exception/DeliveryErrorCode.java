package com.vroomvroom.delivery.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeliveryErrorCode {

    // DeliveryManager
    INVALID_MANAGER_SEQUENCE(HttpStatus.BAD_REQUEST, "잘못된 배송담당자 순번입니다."),
    INVALID_MANAGER_TYPE(HttpStatus.BAD_REQUEST, "잘못된 담당자 타입입니다."),
    MANAGER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 배송매니저입니다."),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 허브 ID입니다."),
    HUB_MANAGER_CAPACITY_FULL(HttpStatus.CONFLICT, "허브 매니저 정원이 초과되었습니다."),
    COMPANY_MANAGER_CAPACITY_FULL(HttpStatus.CONFLICT, "해당 허브의 업체매니저 정원이 초과되었습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
