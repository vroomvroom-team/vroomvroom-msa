package com.vroomvroom.hub.presentation.dto.response;

import com.vroomvroom.hub.domain.entity.Stock;
import com.vroomvroom.hub.domain.vo.ProductId;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateStockRes {
    private ProductId productId;
    private Long quantity;

    public static CreateStockRes from(Stock stock) {
        return CreateStockRes.builder()
                .productId(stock.getProductId())
                .quantity(stock.getQuantity())
                .build();
    }
}