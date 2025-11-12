package com.vroomvroom.orderservice.application.service;

import com.vroomvroom.orderservice.infrastructure.dto.StockDTO;

import java.util.UUID;

public interface HubClient {

    StockDTO getStockInfo(UUID hubId,UUID productId);

    void decreaseStocks(UUID productId, long quantity);

    void increaseStocks(UUID productId, long quantity);
}
