package com.vroomvroom.orderservice.domain.vo;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("대기중", 0),
    CONFIRMED("확인완료", 1),
    PROCESSING("처리중", 2),
    SHIPPING("배송중", 3),
    DELIVERED("배송완료", 4),
    CANCELLED("취소됨", -1),
    RETURNED("반품됨", -1);

    private final String description;
    private final int code;

    OrderStatus(String description, int code) {
        this.description = description;
        this.code = code;
    }

    // 주문 상태가 배송중 미만 상태인지 확인
    public boolean isBeforeShipping() {
        return this.code >= PENDING.code && this.code < SHIPPING.code;
    }
}
