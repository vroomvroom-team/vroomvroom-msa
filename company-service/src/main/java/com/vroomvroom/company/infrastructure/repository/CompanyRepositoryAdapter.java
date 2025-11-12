package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final JpaCompanyRepository jpaCompanyRepository;

    @Override
    public Company save(Company company) {
        return jpaCompanyRepository.save(company);
    }

    @Override
    public boolean existsByCompanyName(String companyName) {
        return jpaCompanyRepository.existsByCompanyName(companyName);
    }

    @Override
    public boolean existsByCompanyAddress(String companyAddress) {
        return jpaCompanyRepository.existsByCompanyAddress(companyAddress);
    }

    @Override
    public Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId) {
        return jpaCompanyRepository.findByCompanyIdAndDeletedAtIsNull(companyId);
    }

    @Override
    public Page<Company> searchCompanies(String keyword, Pageable pageable) {
        return jpaCompanyRepository.searchCompanies(keyword, pageable);
    }
}
