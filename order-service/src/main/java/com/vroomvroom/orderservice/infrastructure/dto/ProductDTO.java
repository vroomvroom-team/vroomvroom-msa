package com.vroomvroom.orderservice.infrastructure.dto;

import com.vroomvroom.orderservice.domain.vo.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.UUID;

/**
 * 외부 상품 서비스 도메인 DTO
 *
 * 상품 서비스의 상품 정보 요청
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private UUID productId;
    private Money price;
    private Long stock;
}
