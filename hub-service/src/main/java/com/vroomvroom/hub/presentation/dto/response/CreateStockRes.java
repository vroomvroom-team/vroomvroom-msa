package com.vroomvroom.hub.presentation.dto.response;

import com.vroomvroom.hub.domain.entity.Stock;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateStockRes {
    private UUID productId;
    private Long quantity;

    public static CreateStockRes from(Stock stock) {
        return CreateStockRes.builder()
                .productId(stock.getProductId().getProductId())
                .quantity(stock.getQuantity())
                .build();
    }
}