package com.vroomvroom.company.infrastructure.repository.product;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByCompany(Company company);
}
