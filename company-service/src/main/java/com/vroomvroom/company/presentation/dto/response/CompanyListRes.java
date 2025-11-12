package com.vroomvroom.company.presentation.dto.response;

import com.vroomvroom.company.application.dto.CompanyResult;

import java.util.UUID;

public record CompanyListRes(
        UUID companyId,
        UUID hubId,
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
) {
    public static CompanyListRes from(CompanyResult result) {
        return new CompanyListRes(
                result.companyId(),
                result.hubId(),
                result.companyManagerId(),
                result.companyName(),
                result.companyAddress(),
                result.companyType()
        );
    }
}
