package com.vroomvroom.hub.application.dto;

import com.vroomvroom.hub.domain.entity.Stock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class StockRes {

    private UUID stockId;
    private UUID productId;
    private Long quantity;

    public static StockRes from(Stock stock) {
        return StockRes.builder()
                .stockId(stock.getStockId())
                .productId(stock.getProductId().getProductId())
                .quantity(stock.getQuantity())
                .build();
    }

    public static List<StockRes> fromList(List<Stock> stocks) {
        return stocks.stream()
                .map(StockRes::from)
                .collect(Collectors.toList());
    }
}