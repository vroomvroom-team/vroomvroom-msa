package com.vroomvroom.company.presentation.dto.response;

import com.vroomvroom.company.application.dto.ProductResult;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductDetailRes {
    private final UUID productId;
    private final UUID companyId;
    private final UUID hubId;
    private final String productName;
    private final Long price;

    public static ProductDetailRes from(ProductResult result) {
        return ProductDetailRes.builder()
                .productId(result.getProductId())
                .companyId(result.getCompanyId())
                .hubId(result.getHubId())
                .productName(result.getProductName())
                .price(result.getPrice())
                .build();
    }
}
