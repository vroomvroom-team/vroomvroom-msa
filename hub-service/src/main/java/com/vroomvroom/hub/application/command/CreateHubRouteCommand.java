package com.vroomvroom.hub.application.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateHubRouteCommand {
    private String routeName;
    private UUID departureHubId;
    private UUID arrivalHubId;
    private Long time;
    private Long distance;

    public CreateHubRouteCommand(String routeName, UUID departureHubId, UUID arrivalHubId, Long time, Long distance) {
        this.routeName = routeName;
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
        this.time = time;
        this.distance = distance;
    }
}
