package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final JpaCompanyRepository jpaCompanyRepository;
}
