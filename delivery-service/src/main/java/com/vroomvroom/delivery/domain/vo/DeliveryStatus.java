package com.vroomvroom.delivery.domain.vo;

public enum DeliveryStatus {

    HUB_WAITING("허브 대기"),
    TRANSIT_HUB("허브 이동"),
    HUB_ARRIVED("허브 도착"),
    DELIVERY_PREPARING("배송 준비"),
    DELIVERY_IN_PROGRESS("배송중"),
    DELIVERY_COMPLETED("배송 완료"),
    CANCELED("배송 취소");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}
