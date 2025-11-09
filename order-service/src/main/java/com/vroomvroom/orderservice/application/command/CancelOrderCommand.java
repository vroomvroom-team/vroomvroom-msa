package com.vroomvroom.orderservice.application.command;

import java.util.UUID;

public record CancelOrderCommand(
        UUID userId,
        UUID orderId
) {
}
