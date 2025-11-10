package com.vroomvroom.company.application.command;

import java.util.UUID;

public record CreateCompanyCommand(
        UUID hubId,
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
        // TODO. userRole 추가
) {}
