package com.vroomvroom.hub.application.dto;

import com.vroomvroom.hub.domain.entity.Hub;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HubListRes {
    private UUID hubId;
    private String hubName;
    private String address;

    public static HubListRes from(Hub hub) {
        return HubListRes.builder()
                .hubId(hub.getHubId())
                .hubName(hub.getHubName())
                .address(hub.getAddress())
                .build();
    }
}