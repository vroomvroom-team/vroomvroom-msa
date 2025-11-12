package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.entity.Product;
import com.vroomvroom.company.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId) {
        return jpaProductRepository.findByProductIdAndDeletedAtIsNull(productId);
    }

    @Override
    public boolean existsByProductNameAndDeletedAtIsNull(String productName) {
        return jpaProductRepository.existsByProductNameAndDeletedAtIsNull(productName);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return jpaProductRepository.searchProducts(keyword, pageable);
    }
}
