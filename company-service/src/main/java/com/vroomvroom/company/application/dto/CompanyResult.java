package com.vroomvroom.company.application.dto;

import com.vroomvroom.company.domain.entity.Company;

import java.util.UUID;

public record CompanyResult (
        UUID companyId,
        UUID hubId,
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
) {
    public static CompanyResult from(Company company) {
        return new CompanyResult(
                company.getCompanyId(),
                company.getHubId(),
                company.getCompanyManagerId(),
                company.getCompanyName(),
                company.getCompanyAddress(),
                company.getCompanyType().name()
        );
    }
}
