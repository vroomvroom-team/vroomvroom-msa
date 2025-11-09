package com.vroomvroom.orderservice.application.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateOrderCommand(
        UUID supplyCompanyId,
        UUID receiveCompanyId,
        UUID productId,
        Long quantity,
        LocalDateTime deadline,
        String requestNote
) {}
