package com.vroomvroom.company.application.dto;

import com.vroomvroom.company.domain.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductResult {
    private final UUID productId;
    private final UUID companyId;
    private final UUID hubId;
    private final String productName;
    private final Long price;

    public static ProductResult from(Product product) {
        return ProductResult.builder()
                .productId(product.getProductId())
                .companyId(product.getCompany().getCompanyId())
                .hubId(product.getHubId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .build();
    }
}
