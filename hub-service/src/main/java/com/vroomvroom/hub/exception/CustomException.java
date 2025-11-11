package com.vroomvroom.hub.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final HubErrorCode hubErrorCode;

    public CustomException(HubErrorCode hubErrorCode) {
        super(hubErrorCode.getMessage());
        this.hubErrorCode = hubErrorCode;
    }
}