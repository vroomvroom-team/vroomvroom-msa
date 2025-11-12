package com.vroomvroom.company.presentation.dto.response;

import com.vroomvroom.company.application.dto.CompanyHubIdResult;

import java.util.UUID;

public record CompanyHubIdRes (
        UUID hubId
) {
    public static CompanyHubIdRes from(CompanyHubIdResult result) {
        return new CompanyHubIdRes(result.hubId());
    }
}
