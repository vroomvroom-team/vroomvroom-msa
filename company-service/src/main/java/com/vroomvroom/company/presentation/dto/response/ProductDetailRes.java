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
    public static ProductDetailRes from(ProductResult result) {
        return new ProductDetailRes(
                result.productId(),
                result.companyId(),
                result.hubId(),
                result.productName(),
                result.price()
        );
    }
}
