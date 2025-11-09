package com.vroomvroom.company.domain.repository;

import com.vroomvroom.company.domain.entity.Company;

public interface CompanyRepository {
    Company save(Company company);
    boolean existsByCompanyName(String companyName);
    boolean existsByCompanyAddress(String companyAddress);
}
