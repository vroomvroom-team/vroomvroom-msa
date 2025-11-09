package com.vroomvroom.hub.presentation.dto.request;

import com.vroomvroom.hub.domain.vo.HubZone;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UpdateHubReq {
    private String hubName;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private HubZone hubZone;
}