package com.vroomvroom.hub.application.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CreateStockCommand {
    private UUID hubId;
    private UUID productId;
    private Long quantity;

    public CreateStockCommand(UUID hubId, UUID productId, Long quantity) {
        this.hubId = hubId;
        this.productId = productId;
        this.quantity = quantity;
    }
}