package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.orderservice.infrastructure.dto.StockDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        name = "hub-service",
        path = "/api/v1/stock"
)
public interface HubFeignClient {
    /**
     * 재고 확인
     */
    @GetMapping("/{hubId}/stock/{productId}")
    ApiResponse<StockDTO> getStockInfo(
            @PathVariable UUID hubId,
            @PathVariable UUID productId
    );

    /**
     * 재고 감소
     */
    @PostMapping("/{hubId}/stock/decrease")
    ApiResponse<Void> decreaseStock(
            @PathVariable UUID hubId,
            @RequestParam Long quantity
    );

    /**
     * 재고 증가
     */
    @PostMapping("/{hubId}/stock/increase")
    ApiResponse<Void> increaseStock(
            @PathVariable UUID hubId,
            @RequestParam Long quantity
    );
}
