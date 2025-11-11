package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.orderservice.infrastructure.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "product-service",
        path = "/api/v1/products"
)
public interface ProductFeignClient {
    /**
     * 상품 정보 조회
     */
    @GetMapping("/{productId}")
    ApiResponse<ProductDTO> getProductInfo(@PathVariable UUID productId);
}
