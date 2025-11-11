package com.vroomvroom.company.presentation;

import com.vroomvroom.company.application.command.CreateProductCommand;
import com.vroomvroom.company.application.dto.ProductResult;
import com.vroomvroom.company.application.service.ProductService;
import com.vroomvroom.company.common.api.ApiResponse;
import com.vroomvroom.company.presentation.dto.request.CreateProductReq;
import com.vroomvroom.company.presentation.dto.response.ProductDetailRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResult>> createProduct(
            @RequestBody @Valid CreateProductReq req
/*             TODO. 유저 정보 받아오기
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String userRole*/
    ) {
        log.info("POST api/v1/products 상품 등록 요청");

        CreateProductCommand command = new CreateProductCommand(
                req.companyId(),
                req.hubId(),
                req.productName(),
                req.price()
                // TODO. userId, userRole 추가
        );

        ProductResult response = ProductDetailRes.from(productService.createProduct(command));

        log.info("상품 등록 성공: productId = {}", response.productId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
