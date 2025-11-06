package com.vroomvroom.hub.application.dto;

import com.vroomvroom.hub.domain.entity.Hub;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class HubRes {

    @Getter
    @Builder
    public static class CreateHubRes {
        private UUID hubId;
        private String hubName;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private LocalDateTime createdAt;
        private String createdBy;

        public static CreateHubRes from(Hub hub) {
            return CreateHubRes.builder()
                    .hubId(hub.getHubId())
                    .hubName(hub.getHubName())
                    .address(hub.getAddress())
                    .latitude(hub.getLocation().getLatitude())
                    .longitude(hub.getLocation().getLongitude())
                    .createdAt(hub.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class HubListRes {
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
}
