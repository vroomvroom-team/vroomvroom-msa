package com.vroomvroom.hub.application.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateHubRouteCommand {
    private UUID departureHubId;
    private UUID arrivalHubId;

    public CreateHubRouteCommand(UUID departureHubId, UUID arrivalHubId) {
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
    }
}
