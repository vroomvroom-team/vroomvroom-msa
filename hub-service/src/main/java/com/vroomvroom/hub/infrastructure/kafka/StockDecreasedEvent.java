package com.vroomvroom.hub.infrastructure.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockDecreasedEvent {
    private UUID orderId;
    private UUID hubId;
    private UUID productId;
    private Long quantity;
    private Long timestamp;
}