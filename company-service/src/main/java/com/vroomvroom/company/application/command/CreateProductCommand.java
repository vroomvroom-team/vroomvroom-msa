package com.vroomvroom.company.application.command;

import java.util.UUID;

public record CreateProductCommand (
        UUID companyId,
        UUID hubId,
        String productName,
        Long price
) {}
