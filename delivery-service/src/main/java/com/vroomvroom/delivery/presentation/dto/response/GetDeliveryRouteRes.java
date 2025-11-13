package com.vroomvroom.delivery.presentation.dto.response;

import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import java.util.UUID;
import lombok.Getter;

@Getter
public class GetDeliveryRouteRes {

    private UUID id;
    private Long deliveryManagerId;
    private UUID startHubId;
    private UUID arriveHubId;
    private GetDeliveryRes delivery;
    private Long expectedDistance; // 미터(m)
    private Long expectedDuration; // 초(s)
    private Long actualDistance; // 미터(m)
    private Long actualDuration; // 초(s)
    private String status;

    public GetDeliveryRouteRes(
        UUID id, Long deliveryManagerId, UUID startHubId, UUID arriveHubId,
        GetDeliveryRes delivery, Long expectedDistance, Long expectedDuration,
        Long actualDistance, Long actualDuration, String status
    ) {
        this.id = id;
        this.deliveryManagerId = deliveryManagerId;
        this.startHubId = startHubId;
        this.arriveHubId = arriveHubId;
        this.delivery = delivery;
        this.expectedDistance = expectedDistance;
        this.expectedDuration = expectedDuration;
        this.actualDistance = actualDistance;
        this.actualDuration = actualDuration;
        this.status = status;
    }

    public static GetDeliveryRouteRes from(DeliveryRoute route) {
        return new GetDeliveryRouteRes(
            route.getId(),
            route.getDeliveryManagerId(),
            route.getStartHubId().getId(),
            route.getArriveHubId().getId(),
            GetDeliveryRes.from(route.getDelivery()),
            route.getExpectedDistance(),
            route.getExpectedDuration(),
            route.getActualDistance(),
            route.getActualDuration(),
            route.getStatus().name()
        );
    }
}
