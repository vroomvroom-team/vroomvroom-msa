package com.vroomvroom.hub.application.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateHubRouteCommand {
    private UUID departureHubId;
    private UUID arrivalHubId;
    private Long time;
    private Long distance;

    public CreateHubRouteCommand(UUID departureHubId, UUID arrivalHubId, Long time, Long distance) {
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
        this.time = time;
        this.distance = distance;
    }
}
