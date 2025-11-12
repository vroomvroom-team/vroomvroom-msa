package com.vroomvroom.company.presentation.dto.response;

import com.vroomvroom.company.application.dto.CompanyResult;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CompanyListRes {
    private final UUID companyId;
    private final UUID hubId;
    private final Long companyManagerId;
    private final String companyName;
    private final String companyAddress;
    private final String companyType;

    public static CompanyListRes from(CompanyResult result) {
        return CompanyListRes.builder()
                .companyId(result.getCompanyId())
                .hubId(result.getHubId())
                .companyManagerId(result.getCompanyManagerId())
                .companyName(result.getCompanyName())
                .companyAddress(result.getCompanyAddress())
                .companyType(result.getCompanyType())
                .build();
    }
}
