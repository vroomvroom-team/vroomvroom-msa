package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByCompanyName(String companyName);
    boolean existsByCompanyAddress(String companyAddress);
    Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId);

    @Query("SELECT c FROM Company c " +
            "WHERE c.deletedAt IS NULL " +
            "AND (:keyword IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Company> searchCompanies(String keyword, Pageable pageable);
}
