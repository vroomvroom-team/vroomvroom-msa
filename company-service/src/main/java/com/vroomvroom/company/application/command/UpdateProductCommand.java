package com.vroomvroom.company.application.command;

import java.util.UUID;

public record UpdateProductCommand (
        UUID productId,
        String productName,
        Long price
) {}
