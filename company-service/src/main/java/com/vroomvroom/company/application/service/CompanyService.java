package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.command.DeleteCommand;
import com.vroomvroom.company.application.command.UpdateCompanyCommand;
import com.vroomvroom.company.application.dto.CompanyResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CompanyService {
    CompanyResult createCompany(CreateCompanyCommand command);
    CompanyResult getCompany(UUID companyId);
    CompanyResult updateCompany(UpdateCompanyCommand command);
    void deleteCompany(DeleteCommand command);
    Page<CompanyResult> getCompanyList(String keyword, Pageable pageable);
}