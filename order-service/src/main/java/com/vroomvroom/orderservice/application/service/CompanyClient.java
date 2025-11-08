package com.vroomvroom.orderservice.application.service;

import com.vroomvroom.orderservice.infrastructure.dto.CompanyHubDTO;

import java.util.UUID;

public interface CompanyClient {
    CompanyHubDTO getCompanyHubInfo(UUID companyId);
}
