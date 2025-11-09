package com.vroomvroom.hub.application.command;

import com.vroomvroom.hub.domain.vo.HubZone;
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
    private HubZone hubZone;
    private boolean isCentral;

    public CreateHubCommand(String hubName, String address, BigDecimal latitude, BigDecimal longitude, HubZone hubZone, boolean isCentral) {
        this.hubName = hubName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hubZone = hubZone;
        this.isCentral = isCentral;
    }
}