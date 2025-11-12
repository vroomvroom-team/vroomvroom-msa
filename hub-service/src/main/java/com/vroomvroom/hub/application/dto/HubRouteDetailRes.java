package com.vroomvroom.hub.application.dto;

import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.entity.HubRoute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteDetailRes {
    private UUID routeId;
    private String routeName;
    private HubInfoRes departureHub;
    private HubInfoRes arrivalHub;
    private Long time;
    private Long distance;
    private Boolean isActive;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HubInfoRes {
        private UUID hubId;
        private String hubName;
        private String address;

        public static HubInfoRes from(Hub hub) {
            return HubInfoRes.builder()
                    .hubId(hub.getHubId())
                    .hubName(hub.getHubName())
                    .address(hub.getAddress())
                    .build();
        }
    }

    public static HubRouteDetailRes from(HubRoute hubRoute) {
        return HubRouteDetailRes.builder()
                .routeId(hubRoute.getRouteId())
                .routeName(hubRoute.getRouteName())
                .departureHub(HubInfoRes.from(hubRoute.getDepartureHub()))
                .arrivalHub(HubInfoRes.from(hubRoute.getArrivalHub()))
                .time(hubRoute.getTime())
                .distance(hubRoute.getDistance())
                .isActive(hubRoute.getIsActive())
                .build();
    }
}
