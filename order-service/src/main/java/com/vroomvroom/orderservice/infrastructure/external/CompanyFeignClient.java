package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.orderservice.infrastructure.dto.CompanyHubDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "company-service",
        path = "/api/v1/companies"
)
public interface CompanyFeignClient {
    /**
     * 업체의 소속 허브 정보 조회
     */
    @GetMapping("/{companyId}")
    ApiResponse<CompanyHubDTO> getCompanyHubInfo(@PathVariable UUID companyId);
}
