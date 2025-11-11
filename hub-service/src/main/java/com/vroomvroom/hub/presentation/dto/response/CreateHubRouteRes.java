package com.vroomvroom.hub.presentation.dto.response;

import com.vroomvroom.hub.domain.entity.HubRoute;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateHubRouteRes {
    private UUID routeId;
    private String departureHubName;
    private String departureAddress;
    private String arrivalHubName;
    private String arrivalHubAddress;
    private Long time;
    private Long distance;

    public static CreateHubRouteRes from(HubRoute hubRoute) {
        return CreateHubRouteRes.builder()
                .routeId(hubRoute.getRouteId())
                .departureHubName(hubRoute.getDepartureHub().getHubName())
                .departureAddress(hubRoute.getDepartureHub().getAddress())
                .arrivalHubName(hubRoute.getArrivalHub().getHubName())
                .arrivalHubAddress(hubRoute.getArrivalHub().getAddress())
                .time(hubRoute.getTime())
                .distance(hubRoute.getDistance())
                .build();
    }
}
