package com.vroomvroom.orderservice.application.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateOrderCommand(
        UUID userId,
        UUID orderId,
        Long quantity,
        LocalDateTime deadline,
        String requestNote
) {
}
