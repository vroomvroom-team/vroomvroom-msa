package com.vroomvroom.delivery.domain.vo;

public enum DeliveryRouteStatus {
    HUB_MOVE_WAITING("허브 대기"),
    HUB_MOVING("허브 이동"),
    HUB_ARRIVED("허브 도착"),
    DELIVERY_IN_PROGRESS("배송 중"),
    COMPLETED("배송 완료");

    private final String description;

    DeliveryRouteStatus(String description) {
        this.description = description;
    }
}
