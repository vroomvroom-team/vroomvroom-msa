package com.vroomvroom.delivery.infrastructure.external.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class HubDTO {

    private UUID startHubId;
    private UUID arriveHubId;
    private Long time; // 초(s)
    private Long distance; // 미터(m)
}
