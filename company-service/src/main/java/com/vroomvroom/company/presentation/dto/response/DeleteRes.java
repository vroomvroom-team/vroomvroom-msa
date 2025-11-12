package com.vroomvroom.company.presentation.dto.response;

import java.util.UUID;

public record DeleteRes (
        UUID id,
        String message
) {}
