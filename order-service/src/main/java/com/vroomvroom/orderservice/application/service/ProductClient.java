package com.vroomvroom.orderservice.application.service;

import com.vroomvroom.orderservice.infrastructure.dto.ProductDTO;

import java.util.UUID;

public interface ProductClient {
    ProductDTO getProductInfo(UUID productId);

    boolean decreaseStocks(UUID productId, long quantity);

    void increaseStocks(UUID productId, long quantity);
}
