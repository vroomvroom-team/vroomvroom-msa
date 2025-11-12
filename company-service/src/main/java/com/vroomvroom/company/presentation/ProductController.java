package com.vroomvroom.company.presentation;

import com.vroomvroom.company.application.command.CreateProductCommand;
import com.vroomvroom.company.application.command.DeleteCommand;
import com.vroomvroom.company.application.command.UpdateProductCommand;
import com.vroomvroom.company.application.dto.ProductResult;
import com.vroomvroom.company.application.service.ProductService;
import com.vroomvroom.company.common.api.ApiResponse;
import com.vroomvroom.company.common.api.PageResponse;
import com.vroomvroom.company.presentation.dto.request.CreateProductReq;
import com.vroomvroom.company.presentation.dto.request.UpdateProductReq;
import com.vroomvroom.company.presentation.dto.response.DeleteRes;
import com.vroomvroom.company.presentation.dto.response.ProductDetailRes;
import com.vroomvroom.company.presentation.dto.response.ProductListRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDetailRes>> createProduct(
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

        ProductDetailRes response = ProductDetailRes.from(productService.createProduct(command));

        log.info("상품 등록 성공: productId = {}", response.productId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailRes>> getProduct(@PathVariable UUID productId) {
        log.info("GET api/v1/products/{} 상품 상세조회 요청", productId);

        ProductDetailRes response = ProductDetailRes.from(productService.getProduct(productId));

        log.info("상품 상세 조회 성공: companyId = {}", response.productId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductListRes>>> getProductList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("GET api/v1/products 상품 목록 조회 요청");
        Page<ProductResult> productPage = productService.getProductList(keyword, pageable);

        Page<ProductListRes> productListPage = productPage.map(ProductListRes::from);

        PageResponse<ProductListRes> response = PageResponse.fromPage(productListPage);

        log.info("상품 목록 조회 성공");
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailRes>> updateProduct(
            @PathVariable UUID productId,
            @RequestBody UpdateProductReq req
/*             TODO. 유저 정보 받아오기
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String userRole*/

    ) {
        log.info("PATCH api/v1/products/{} 상품 수정 요청", productId);

        UpdateProductCommand command = new UpdateProductCommand(
                productId,
                req.productName(),
                req.price()
                // TODO. userId, userRole 추가
        );

        ProductDetailRes response = ProductDetailRes.from(productService.updateProduct(command));

        log.info("상품 수정 성공: productId = {}", response.productId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<DeleteRes>> deleteProduct(
            @PathVariable UUID productId
/*             TODO. 유저 정보 받아오기
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String userRole*/
    ) {
        log.info("DELETE api/v1/products/{} 상품 삭제 요청", productId);

        DeleteCommand command = new DeleteCommand(
                productId
                // TODO. userId, userRole 추가
        );

        productService.deleteCompany(command);

        DeleteRes response = new DeleteRes(
                productId,
                "상품이 성공적으로 삭제되었습니다."
        );

        log.info("상품 삭제 성공: productId = {}", productId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
