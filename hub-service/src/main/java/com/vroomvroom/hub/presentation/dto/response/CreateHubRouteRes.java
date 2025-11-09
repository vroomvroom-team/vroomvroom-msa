package com.vroomvroom.hub.presentation.dto.response;

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
}
