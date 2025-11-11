package com.vroomvroom.hub.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DecreaseStockReq {
    private UUID productId;
    private Long quantity;
    private UUID orderId;
}