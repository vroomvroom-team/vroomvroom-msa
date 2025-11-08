package com.vroomvroom.orderservice.application.command;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateOrderCommand(
        UUID supplyCompanyId,
        UUID receiveCompanyId,
        UUID productId,
        BigInteger quantity,
        LocalDateTime deadline,
        String requestNote
) {}
