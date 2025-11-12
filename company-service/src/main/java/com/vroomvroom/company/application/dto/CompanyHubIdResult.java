package com.vroomvroom.company.application.dto;

import java.util.UUID;

public record CompanyHubIdResult (UUID hubId) {
    public static CompanyHubIdResult from(UUID hubId) {
        return new CompanyHubIdResult(hubId);
    }
}