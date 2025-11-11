package com.vroomvroom.company.application.dto;

import com.vroomvroom.company.domain.entity.Product;

import java.util.UUID;

public record ProductResult (
        UUID productId,
        UUID companyId,
        UUID hubId,
        String productName,
        Long price
) {
    public static ProductResult from(Product product) {
        return new ProductResult(
                product.getProductId(),
                product.getCompany().getCompanyId(),
                product.getHubId(),
                product.getProductName(),
                product.getPrice()
        );
    }
}