package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateCompanyCommand;

import java.util.UUID;

public interface CompanyService {
    UUID createCompany(CreateCompanyCommand command);
}