package com.vroomvroom.company.presentation.dto.response;

import com.vroomvroom.company.application.dto.CompanyResult;

import java.util.UUID;

public record CompanyDetailRes(
        UUID companyId,
        UUID hubId,
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
) {
    public static CompanyDetailRes from(CompanyResult result) {
        return new CompanyDetailRes(
                result.companyId(),
                result.hubId(),
                result.companyManagerId(),
                result.companyName(),
                result.companyAddress(),
                result.companyType()
        );
    }
}
