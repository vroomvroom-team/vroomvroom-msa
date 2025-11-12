package com.vroomvroom.company.application.command;

import java.util.UUID;

public record DeleteCommand (
        UUID id
        // TODO. userId, userRole 추가
) {}

