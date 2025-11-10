package com.vroomvroom.hub.application.dto;

import com.vroomvroom.hub.domain.entity.HubRoute;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HubRouteListRes {
    private UUID routeId;
    private String routeName;
    private String departureHubName;
    private String arrivalHubName;
    private Long time;
    private Long distance;

    public static HubRouteListRes from(HubRoute hubRoute) {
        return HubRouteListRes.builder()
                .routeId(hubRoute.getRouteId())
                .routeName(hubRoute.getRouteName())
                .departureHubName(hubRoute.getDepartureHub().getHubName())
                .arrivalHubName(hubRoute.getArrivalHub().getHubName())
                .time(hubRoute.getTime())
                .distance(hubRoute.getDistance())
                .build();
    }
}
