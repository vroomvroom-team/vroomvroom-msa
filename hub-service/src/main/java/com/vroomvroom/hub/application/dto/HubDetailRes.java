package com.vroomvroom.hub.application.dto;

import com.vroomvroom.hub.domain.entity.Hub;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class HubDetailRes {
    private UUID hubId;
    private String hubName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static HubDetailRes from(Hub hub) {
        return HubDetailRes.builder()
                .hubId(hub.getHubId())
                .hubName(hub.getHubName())
                .address(hub.getAddress())
                .latitude(hub.getLocation().getLatitude())
                .longitude(hub.getLocation().getLongitude())
                .build();
    }
}