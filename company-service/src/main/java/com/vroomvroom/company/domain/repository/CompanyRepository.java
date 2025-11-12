package com.vroomvroom.company.domain.repository;

import com.vroomvroom.company.domain.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Company save(Company company);
    boolean existsByCompanyName(String companyName);
    boolean existsByCompanyAddress(String companyAddress);
    Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId);
    Page<Company> searchCompanies(String keyword, Pageable pageable);
}
