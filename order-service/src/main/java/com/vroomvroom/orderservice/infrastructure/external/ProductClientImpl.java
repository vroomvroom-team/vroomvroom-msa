package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.orderservice.application.service.ProductClient;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.infrastructure.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    private final ProductFeignClient productFeignClient;

    @Override
    public ProductDTO getProductInfo(UUID productId) {
        log.info("상품 정보 조회 요청 : productId = {}", productId);

        // TODO. EUREKA Server 적용 필요
        /*
        try {
            ApiResponse<ProductDTO> response = productFeignClient.getProductInfo(productId);

            if (!response.isSuccess() || response.getData() == null) {
                log.error("상품 정보 조회 실패 : productId = {}", productId);
                throw new CustomException(OrderErrorCode.PRODUCT_INTERNAL_SERVER_ERROR);
            }

            log.info("상품 정보 조회 성공 : productId = {}", productId);
            return response.getData();
        } catch (FeignException.NotFound e) {
            log.error("상품을 찾을 수 없음 : productId = {}", productId);
            throw new CustomException(OrderErrorCode.PRODUCT_NOT_FOUND);
        } catch (FeignException e) {
            log.error("상품 서비스 호출 실패 : status={}, message={}", e.status(), e.getMessage());
            throw new CustomException(OrderErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
        }
        */

        // TODO. EUREKA Server 적용 시 삭제
        return ProductDTO.builder()
                .productId(productId)
                .price(Money.of(10000)) // TODO. 상품 가격 반영 필요
                .stock(10L) // TODO. 상품 재고 반영 필요
                .build();
    }
}
