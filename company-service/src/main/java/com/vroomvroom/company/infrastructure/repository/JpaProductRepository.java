package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByCompany(Company company);
    Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId);
    boolean existsByProductNameAndDeletedAtIsNull(String productName);

    @Query("SELECT p FROM Product p " +
            "WHERE p.deletedAt IS NULL " +
            "AND (:keyword IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(String keyword, Pageable pageable);
}
