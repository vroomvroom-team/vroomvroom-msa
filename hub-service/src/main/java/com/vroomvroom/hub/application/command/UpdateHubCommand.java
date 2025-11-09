package com.vroomvroom.hub.application.command;

import com.vroomvroom.hub.domain.vo.HubZone;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UpdateHubCommand {

    private String hubName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private HubZone hubZone;

    public UpdateHubCommand(String hubName, String address, BigDecimal latitude, BigDecimal longitude, HubZone hubZone) {
        this.hubName = hubName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hubZone = hubZone;
    }
}
