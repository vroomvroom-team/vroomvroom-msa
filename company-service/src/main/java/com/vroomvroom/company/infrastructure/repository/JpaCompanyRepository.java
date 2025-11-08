package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.vo.CompanyAddress;
import com.vroomvroom.company.domain.vo.CompanyName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByCompanyName(CompanyName companyName);
    boolean existsByCompanyAddress(CompanyAddress companyAddress);
}
