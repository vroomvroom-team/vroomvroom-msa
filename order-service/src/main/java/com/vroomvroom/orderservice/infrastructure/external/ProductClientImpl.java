package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.orderservice.application.service.ProductClient;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.infrastructure.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.UUID;

@Component
public class ProductClientImpl implements ProductClient {
    @Override
    public ProductDTO getProductInfo(UUID productId) {
        // TODO. FeignClient를 통한 API 호출
        return ProductDTO.builder()
                .productId(productId)
                .price(Money.of(10000)) // TODO. 상품 가격 반영 필요
                .stock(BigInteger.valueOf(10)) // TODO. 상품 재고 반영 필요
                .build();
    }

    @Override
    public boolean decreaseStocks(UUID productId, BigInteger quantity) {
        // TODO. FeignClient를 통한 재고 감소 API 호출?
        return true;
    }

    @Override
    public void increaseStocks(UUID productId, BigInteger abs) {
        // TODO. FeignClient를 통한 재고 증가 API 호출?
    }
}
