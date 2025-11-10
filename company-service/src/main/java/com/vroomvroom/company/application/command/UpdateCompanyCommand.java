package com.vroomvroom.company.application.command;

import java.util.UUID;

public record UpdateCompanyCommand(
        UUID companyId,
        UUID hubId,
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
) {}
