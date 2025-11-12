package com.vroomvroom.company.presentation.dto.request;

public record UpdateProductReq (
        String productName,
        Long price
) {}
