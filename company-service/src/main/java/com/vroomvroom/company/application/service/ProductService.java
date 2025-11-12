package com.vroomvroom.company.application.service;

import com.vroomvroom.company.common.api.PageResponse;
import com.vroomvroom.company.application.command.CreateProductCommand;
import com.vroomvroom.company.application.command.DeleteCommand;
import com.vroomvroom.company.application.command.UpdateProductCommand;
import com.vroomvroom.company.application.dto.ProductResult;
import com.vroomvroom.company.presentation.dto.response.ProductListRes;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    ProductResult createProduct(CreateProductCommand command);
    ProductResult getProduct(UUID productId);
    ProductResult updateProduct(UpdateProductCommand command);
    void deleteCompany(DeleteCommand command);
    PageResponse<ProductListRes> getProductList(String keyword, Pageable pageable);
}
