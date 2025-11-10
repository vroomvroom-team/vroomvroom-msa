package com.vroomvroom.company.presentation.dto.request;

public record UpdateCompanyReq (
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
) {}