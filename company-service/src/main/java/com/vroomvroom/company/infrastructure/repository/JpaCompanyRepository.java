package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByCompanyName(String companyName);
    boolean existsByCompanyAddress(String companyAddress);
    Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId);
}
