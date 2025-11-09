package com.vroomvroom.orderservice.application.command;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateOrderCommand(
        UUID userId,
        UUID orderId,
        BigInteger quantity,
        LocalDateTime deadline,
        String requestNote
) {
}
