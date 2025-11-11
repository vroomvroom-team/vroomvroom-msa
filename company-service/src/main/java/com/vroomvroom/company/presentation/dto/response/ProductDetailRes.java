package com.vroomvroom.company.presentation.dto.response;

import com.vroomvroom.company.application.dto.ProductResult;

import java.util.UUID;

public record ProductDetailRes(
        UUID productId,
        UUID companyId,
        UUID hubId,
        String productName,
        Long price
) {
    public static ProductResult from(ProductResult result) {
        return new ProductResult(
                result.productId(),
                result.companyId(),
                result.hubId(),
                result.productName(),
                result.price()
        );
    }
}
