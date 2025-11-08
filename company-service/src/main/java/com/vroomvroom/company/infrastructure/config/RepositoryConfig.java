package com.vroomvroom.company.infrastructure.config;

import com.vroomvroom.company.domain.repository.CompanyRepository;
import com.vroomvroom.company.infrastructure.repository.CompanyRepositoryAdapter;
import com.vroomvroom.company.infrastructure.repository.JpaCompanyRepository;
import org.springframework.context.annotation.Bean;

public class RepositoryConfig {

    @Bean
    public CompanyRepository companyRepository(JpaCompanyRepository jpaCompanyRepository) {
        return new CompanyRepositoryAdapter(jpaCompanyRepository);
    }
}
