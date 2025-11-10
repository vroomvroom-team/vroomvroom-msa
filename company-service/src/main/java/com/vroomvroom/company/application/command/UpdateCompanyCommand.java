package com.vroomvroom.company.application.command;

import java.util.UUID;

public record UpdateCompanyCommand(
        UUID companyId,
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
        // TODO. userId, userRole 추가
) {}
