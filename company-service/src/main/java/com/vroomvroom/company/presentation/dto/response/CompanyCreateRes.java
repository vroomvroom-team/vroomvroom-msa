package com.vroomvroom.company.presentation.dto.response;

import java.util.UUID;

public record CompanyCreateRes (
    UUID companyId,
    String message
) {}
