package com.vroomvroom.company.presentation.dto.response;

import java.util.UUID;

public record CompanyDeletedRes(
        UUID companyId,
        String message
) {}
