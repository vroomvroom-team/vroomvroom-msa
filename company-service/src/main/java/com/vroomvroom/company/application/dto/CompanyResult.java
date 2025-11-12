package com.vroomvroom.company.application.dto;

import com.vroomvroom.company.domain.entity.Company;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CompanyResult {
    private final UUID companyId;
    private final UUID hubId;
    private final Long companyManagerId;
    private final String companyName;
    private final String companyAddress;
    private final String companyType;

    public static CompanyResult from(Company company) {
        return CompanyResult.builder()
                .companyId(company.getCompanyId())
                .hubId(company.getHubId())
                .companyManagerId(company.getCompanyManagerId())
                .companyName(company.getCompanyName())
                .companyAddress(company.getCompanyAddress())
                .companyType(company.getCompanyType().name())
                .build();
    }
}
