package com.vroomvroom.delivery.domain.event;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderCreatedEvent {

    @NotNull
    private UUID orderId;

    @NotNull
    private UUID startHubId; // 공급업체 허브 ID

    @NotNull
    private UUID arriveHubId; // 수령업체 허브 ID

    @NotNull
    private String address; // 업체주소

    @NotNull
    private Long receiverId; // 업체 관리자 id

    private OrderCreatedEvent(
        UUID orderId, UUID startHubId, UUID arriveHubId,
        Long companyManagerId, String address
    ) {
        this.orderId = orderId;
        this.startHubId = startHubId;
        this.arriveHubId = arriveHubId;
        this.receiverId = companyManagerId;
        this.address = address;
    }
}
