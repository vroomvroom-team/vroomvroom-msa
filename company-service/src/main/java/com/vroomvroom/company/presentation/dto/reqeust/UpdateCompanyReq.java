package com.vroomvroom.company.presentation.dto.reqeust;

public record UpdateCompanyReq (
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
) {}