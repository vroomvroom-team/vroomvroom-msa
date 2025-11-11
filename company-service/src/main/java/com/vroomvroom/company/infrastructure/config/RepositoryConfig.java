package com.vroomvroom.company.infrastructure.config;

import com.vroomvroom.company.domain.repository.CompanyRepository;
import com.vroomvroom.company.infrastructure.repository.company.CompanyRepositoryAdapter;
import com.vroomvroom.company.infrastructure.repository.company.JpaCompanyRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public CompanyRepository companyRepository(JpaCompanyRepository jpaCompanyRepository) {
        return new CompanyRepositoryAdapter(jpaCompanyRepository);
    }
}
