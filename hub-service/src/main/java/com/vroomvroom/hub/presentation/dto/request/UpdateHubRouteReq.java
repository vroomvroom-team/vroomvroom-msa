package com.vroomvroom.hub.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateHubRouteReq {
    private String routeName;
    private Long time;
    private Long distance;
    private Boolean isActive;
}
