package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.repository.CompanyRepository;
import com.vroomvroom.company.domain.vo.CompanyAddress;
import com.vroomvroom.company.domain.vo.CompanyName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final JpaCompanyRepository jpaCompanyRepository;

    @Override
    public Company save(Company company) {
        return jpaCompanyRepository.save(company);
    }

    @Override
    public boolean existsByCompanyName(CompanyName companyName) {
        return jpaCompanyRepository.existsByCompanyName(companyName);
    }

    @Override
    public boolean existsByCompanyAddress(CompanyAddress companyAddress) {
        return jpaCompanyRepository.existsByCompanyAddress(companyAddress);
    }
}
