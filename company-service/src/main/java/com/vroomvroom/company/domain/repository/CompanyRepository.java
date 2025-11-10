package com.vroomvroom.company.domain.repository;

import com.vroomvroom.company.domain.entity.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Company save(Company company);
    boolean existsByCompanyName(String companyName);
    boolean existsByCompanyAddress(String companyAddress);
    Optional<Company> findByCompanyId(UUID companyId);
}
