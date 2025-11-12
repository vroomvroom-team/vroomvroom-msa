package com.vroomvroom.company.application.dto;

import java.util.UUID;

public record ProductOrderInfoResult (
        UUID hubId,
        Long price
) {
    public static ProductOrderInfoResult from(UUID hubId, Long price){
        return new ProductOrderInfoResult(hubId, price);
    }
}
