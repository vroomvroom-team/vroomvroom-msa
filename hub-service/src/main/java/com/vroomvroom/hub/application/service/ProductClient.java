package com.vroomvroom.hub.application.service;

import com.vroomvroom.hub.infrastructure.external.dto.ProductDTO;

import java.util.UUID;

public interface ProductClient {
    boolean exists(UUID productId);
    ProductDTO getProduct(UUID productId);
}