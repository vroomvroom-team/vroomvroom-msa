package com.vroomvroom.hub.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHubRouteReq {
    @NotNull(message = "출발 허브는 필수입니다.")
    private UUID departureHubId;

    @NotNull(message = "도착 허브는 필수입니다.")
    private UUID arrivalHubId;
}
