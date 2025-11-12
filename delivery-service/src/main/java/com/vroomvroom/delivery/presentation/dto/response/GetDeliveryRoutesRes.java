package com.vroomvroom.delivery.presentation.dto.response;

import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class GetDeliveryRoutesRes {

    private UUID id;
    private Long deliveryManagerId;
    private UUID startHubId;
    private UUID arriveHubId;
    private String status;

    public GetDeliveryRoutesRes(
        UUID id, Long deliveryManagerId, UUID startHubId, UUID arriveHubId, String status
    ) {
        this.id = id;
        this.deliveryManagerId = deliveryManagerId;
        this.startHubId = startHubId;
        this.arriveHubId = arriveHubId;
        this.status = status;
    }

    public static List<GetDeliveryRoutesRes> from(List<DeliveryRoute> deliveryRoutes) {
        return deliveryRoutes.stream().map(route -> new GetDeliveryRoutesRes(
            route.getId(),
            route.getDeliveryManagerId(),
            route.getStartHubId().getId(),
            route.getArriveHubId().getId(),
            route.getStatus().name()
        )).toList();
    }
}
