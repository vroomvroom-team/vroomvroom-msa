package com.vroomvroom.hub.infrastructure.external.dto;

import com.vroomvroom.hub.domain.vo.CompanyId;
import com.vroomvroom.hub.domain.vo.ProductId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private ProductId productId;
    private CompanyId companyId;
    private String productName;
}