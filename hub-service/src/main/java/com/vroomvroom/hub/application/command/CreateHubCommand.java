package com.vroomvroom.hub.application.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateHubCommand {
    private String hubName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long hubManagerId;

    public CreateHubCommand(String hubName, String address, BigDecimal latitude, BigDecimal longitude, Long hubManagerId) {
        this.hubName = hubName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hubManagerId = hubManagerId;
    }
}