package com.vroomvroom.orderservice.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 외부 업체 서비스 도메인 DTO
 *
 * 업체의 소속 허브 정보 요청
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyHubDTO {
    private UUID companyId;
    private UUID hubId;
}
