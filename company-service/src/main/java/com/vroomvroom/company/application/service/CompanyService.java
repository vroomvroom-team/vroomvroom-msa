package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.dto.CompanyResult;

import java.util.UUID;

public interface CompanyService {
    CompanyResult createCompany(CreateCompanyCommand command);
    CompanyResult getCompany(UUID companyId);
}