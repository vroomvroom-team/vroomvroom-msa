package com.vroomvroom.company.domain.repository;

import com.vroomvroom.company.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId);
    boolean existsByProductNameAndDeletedAtIsNull(String productName);
    Page<Product> searchProducts(String keyword, Pageable pageable);
}
