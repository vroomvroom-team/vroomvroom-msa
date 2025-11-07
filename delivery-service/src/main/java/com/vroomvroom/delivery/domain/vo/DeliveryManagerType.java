package com.vroomvroom.delivery.domain.vo;

public enum DeliveryManagerType {
    HUB_MANAGER("허브매니저"),
    COMPANY_MANGER("업체매니저");

    private final String description;

    DeliveryManagerType(String description) {
        this.description = description;
    }
}
