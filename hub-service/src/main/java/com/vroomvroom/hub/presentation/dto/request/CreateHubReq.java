package com.vroomvroom.hub.presentation.dto.request;

import com.vroomvroom.hub.domain.vo.HubZone;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHubReq {
    @NotNull(message = "허브 이름은 필수입니다.")
    private String hubName;

    @NotNull(message = "허브 주소는 필수입니다.")
    private String address;

    @NotNull(message = "허브 위도는 필수입니다.")
    private BigDecimal latitude;

    @NotNull(message = "허브 경도는 필수입니다.")
    private BigDecimal longitude;

    @NotNull
    private HubZone hubZone;

    @NotNull
    private boolean isCentral;
}
