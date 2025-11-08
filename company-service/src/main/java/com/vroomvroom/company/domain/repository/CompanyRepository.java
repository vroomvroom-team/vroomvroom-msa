package com.vroomvroom.company.domain.repository;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.vo.CompanyAddress;
import com.vroomvroom.company.domain.vo.CompanyName;

public interface CompanyRepository {
    Company save(Company company);
    boolean existsByCompanyName(CompanyName companyName);
    boolean existsByCompanyAddress(CompanyAddress companyAddress);
}
