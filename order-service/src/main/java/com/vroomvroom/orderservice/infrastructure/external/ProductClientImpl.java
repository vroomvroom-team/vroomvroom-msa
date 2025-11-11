package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.orderservice.application.service.ProductClient;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.exception.OrderErrorCode;
import com.vroomvroom.orderservice.infrastructure.dto.ProductDTO;
import feign.FeignException;
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

    @Override
    public boolean decreaseStocks(UUID productId, long quantity) {
        log.info("재고 감소 요청 : productId={}, quantity={}", productId, quantity);

        /*
        try {
            ApiResponse<Boolean> response =
                    productFeignClient.decreaseStock(productId, quantity);

            if (!response.isSuccess()) {
                log.error("재고 감소 실패: productId={}, quantity={}",
                        productId, quantity);
                return false;
            }

            return response.getData() != null && response.getData();

        } catch (FeignException.BadRequest e) {
            log.error("재고 부족: productId={}, quantity={}", productId, quantity);
            throw new CustomException(OrderErrorCode.BAD_REQUEST);

        } catch (FeignException e) {
            log.error("재고 감소 API 호출 실패: status={}, message={}",
                    e.status(), e.getMessage());
            throw new CustomException(OrderErrorCode.INTERNAL_SERVER_ERROR);
        }
        */

        return true;
    }

    @Override
    public void increaseStocks(UUID productId, long quantity) {
        log.info("재고 증가 요청 : productId={}, quantity={}", productId, quantity);

        /*
        try {
            productFeignClient.increaseStock(productId, quantity);
            log.info("재고 증가 성공: productId={}, quantity={}", productId, quantity);

        } catch (FeignException e) {
            log.error("재고 증가 실패: productId={}, quantity={}, status={}, message={}",
                    productId, quantity, e.status(), e.getMessage());
        }
        */
    }
}
