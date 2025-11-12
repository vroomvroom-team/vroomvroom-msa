package com.vroomvroom.company.presentation.dto.response;

import com.vroomvroom.company.application.dto.ProductOrderInfoResult;

import java.util.UUID;

public record ProductOrderInfoRes (
        UUID hubId,
        Long price
) {
    public static ProductOrderInfoRes from(ProductOrderInfoResult result) {
        return new ProductOrderInfoRes(result.hubId(), result.price());
    }
}
