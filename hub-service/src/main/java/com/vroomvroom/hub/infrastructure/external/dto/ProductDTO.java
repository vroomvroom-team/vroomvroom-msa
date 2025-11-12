package com.vroomvroom.hub.infrastructure.external.dto;

import com.vroomvroom.hub.domain.vo.CompanyId;
import com.vroomvroom.hub.domain.vo.ProductId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID productId;
    private UUID companyId;
    private String productName;

    public ProductId toProductId() {
        return ProductId.of(productId);
    }

    public CompanyId toCompanyId() {
        return CompanyId.of(companyId);
    }
}