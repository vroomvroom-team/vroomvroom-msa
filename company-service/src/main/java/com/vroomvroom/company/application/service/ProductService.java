package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateProductCommand;
import com.vroomvroom.company.application.dto.ProductResult;

public interface ProductService {
    ProductResult createProduct(CreateProductCommand command);
}
