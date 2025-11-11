package com.vroomvroom.hub.application.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateHubRouteCommand {
    private String routeName;
    private Long time;
    private Long distance;
    private Boolean isActive;

    public UpdateHubRouteCommand(String routeName, Long time, Long distance, Boolean isActive) {
        this.routeName = routeName;
        this.time = time;
        this.distance = distance;
        this.isActive = isActive;
    }
}
