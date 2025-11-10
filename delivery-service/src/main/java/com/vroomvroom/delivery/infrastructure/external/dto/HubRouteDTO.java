package com.vroomvroom.delivery.infrastructure.external.dto;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class HubRouteDTO {

    private UUID startHubId;
    private UUID arriveHubId;
    private Long time; // 초(s)
    private Long distance; // 미터(m)

    public static HubRouteDTO of(UUID startHubId, UUID arriveHubId, Long time, Long distance) {
        return HubRouteDTO.builder()
            .startHubId(startHubId)
            .arriveHubId(arriveHubId)
            .time(time)
            .distance(distance)
            .build();
    }
}
