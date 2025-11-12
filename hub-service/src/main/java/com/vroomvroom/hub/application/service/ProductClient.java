package com.vroomvroom.hub.application.service;

import com.vroomvroom.hub.infrastructure.external.dto.ProductDTO;

import java.util.UUID;

public interface ProductClient {
    ProductDTO getProduct(UUID productId);
}