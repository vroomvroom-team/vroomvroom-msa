package com.vroomvroom.delivery.domain.vo;

public enum DeliveryManagerType {

    // 배송쪽에서는 둘 다 DELIVERY 담당해 헷갈릴 수 있어 허브매니저, 업체매니저로 정의
    HUB_MANAGER("허브매니저"),
    COMPANY_MANAGER("업체배송매니저"); // USER ROLE에서는 이를 DELIVERY_MANAGER라고 부름

    private final String description;

    DeliveryManagerType(String description) {
        this.description = description;
    }
}
