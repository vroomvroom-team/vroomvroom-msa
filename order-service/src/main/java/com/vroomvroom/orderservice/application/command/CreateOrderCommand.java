package com.vroomvroom.orderservice.application.command;

import com.vroomvroom.orderservice.domain.vo.Money;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateOrderCommand(
        UUID supplyCompanyId,
        UUID receiveCompanyId,
        UUID supplyHubId,
        UUID receiveHubId,
        UUID productId,
        Money totalPrice,
        BigInteger quantity,
        LocalDateTime deadline,
        String requestNote
) {}
