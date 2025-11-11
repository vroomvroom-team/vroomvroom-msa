
package com.vroomvroom.hub.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class IncreaseStockCommand {
    private UUID hubId;
    private UUID productId;
    private Long quantity;
    private UUID orderId;
}
