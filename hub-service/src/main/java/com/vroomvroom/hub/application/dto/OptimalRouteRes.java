package com.vroomvroom.hub.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OptimalRouteRes {
    private List<HubRouteDetailRes> path;
    private Long totalCost;
}
