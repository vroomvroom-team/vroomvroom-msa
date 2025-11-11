package com.vroomvroom.hub.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    private String routeName;

    @NotNull(message = "출발 허브는 필수입니다.")
    private UUID departureHubId;

    @NotNull(message = "도착 허브는 필수입니다.")
    private UUID arrivalHubId;

    @NotNull(message = "초(s) 단위의 소요 시간은 필수입니다.")
    @Positive(message = "소요 시간은 양수여야 합니다.")
    private Long time;

    @NotNull(message = "미터(m) 단위의 이동 거리는 필수입니다.")
    @Positive(message = "이동 거리는 양수여야 합니다.")
    private Long distance;
}
