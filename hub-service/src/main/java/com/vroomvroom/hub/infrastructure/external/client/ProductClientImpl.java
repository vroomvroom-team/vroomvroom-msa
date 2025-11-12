package com.vroomvroom.hub.infrastructure.external.client;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.hub.application.command.CreateStockCommand;
import com.vroomvroom.hub.application.service.ProductClient;
import com.vroomvroom.hub.domain.vo.ProductId;
import com.vroomvroom.hub.exception.HubErrorCode;
import com.vroomvroom.hub.infrastructure.external.dto.ProductDTO;
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
    public ProductDTO getProduct(UUID productId) {
        try {
            log.info("상품 서비스 연결 준비 중");
            ApiResponse<ProductDTO> response = productFeignClient.getProduct(productId);
            log.info("Feign 응답: {}", response);
            ProductDTO res = response.getData();
            log.info("상품 서비스 연결 성공: {}", res.getCompanyId().toString());
            if (res == null) throw new CustomException(HubErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
            return res;
        } catch (FeignException.NotFound e) {
            throw new CustomException(HubErrorCode.PRODUCT_NOT_FOUND);
        } catch (FeignException e) {
            log.error("Feign 예외 발생: status: {}, message={}", e.status(), e.getMessage());
            throw new CustomException(HubErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
        }
    }
}