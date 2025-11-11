package com.vroomvroom.company.domain.repository;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAllByCompany(Company company);
    Product save(Product product);
    Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId);
}
