package com.vroomvroom.company.domain.repository;

import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAllByCompany(Company company);
    Product save(Product product);
}
