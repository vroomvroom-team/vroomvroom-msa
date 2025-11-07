package com.vroomvroom.delivery.domain.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final DeliveryErrorCode errorCode;

    public CustomException(DeliveryErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}