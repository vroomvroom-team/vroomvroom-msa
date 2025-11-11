package com.vroomvroom.delivery.domain.exception;

public class AllManagerBusyException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "현재 배정 가능한 담당자가 없습니다.";

    public AllManagerBusyException() {
        super(DEFAULT_MESSAGE);
    }

    public AllManagerBusyException(String message) {
        super(message);
    }
}
