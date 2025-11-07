package com.vroomvroom.company.infrastructure.config;

import com.vroomvroom.company.domain.service.CompanyDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public CompanyDomainService companyDomainService() {
        return new CompanyDomainService();
    }
}
