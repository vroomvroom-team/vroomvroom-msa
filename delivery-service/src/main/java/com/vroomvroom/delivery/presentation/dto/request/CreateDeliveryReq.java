package com.vroomvroom.delivery.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CreateDeliveryReq {

    @NotNull
    private UUID orderId;

    @NotNull
    private UUID startHubId; // 공급업체 허브 ID

    @NotNull
    private UUID arriveHubId; // 수령업체 허브 ID

    @NotNull
    private Long receiverId;

    @NotNull
    private UUID receiverSlackId;

    @NotNull
    private String address;
}
