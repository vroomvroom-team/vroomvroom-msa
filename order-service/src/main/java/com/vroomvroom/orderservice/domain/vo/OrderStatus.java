package com.vroomvroom.orderservice.domain.vo;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("대기중"),
    CONFIRMED("확인완료"),
    PROCESSING("처리중"),
    SHIPPING("배송중"),
    DELIVERED("배송완료"),
    CANCELLED("취소됨"),
    RETURNED("반품됨");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
