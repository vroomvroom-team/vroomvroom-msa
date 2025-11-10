package com.vroomvroom.company.application.command;

import java.util.UUID;

public record DeleteCompanyCommand (
        UUID companyId
        // TODO. userId, userRole 추가
) {}

