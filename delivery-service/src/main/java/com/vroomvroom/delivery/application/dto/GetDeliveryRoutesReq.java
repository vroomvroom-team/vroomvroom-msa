package com.vroomvroom.delivery.application.dto;

import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetDeliveryRoutesReq {
    private final UUID startHubId;
    private final UUID arriveHubId;

    public static GetDeliveryRoutesReq of(UUID startHubId, UUID arriveHubId) {
        return new GetDeliveryRoutesReq(startHubId, arriveHubId);
    }
}