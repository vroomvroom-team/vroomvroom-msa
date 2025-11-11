package com.vroomvroom.company.infrastructure.repository;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.entity.Product;
import com.vroomvroom.company.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public List<Product> findAllByCompany(Company company) {
        return jpaProductRepository.findAllByCompany(company);
    }

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId) {
        return jpaProductRepository.findByProductIdAndDeletedAtIsNull(productId);
    }
}
