package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.orderservice.application.service.CompanyClient;
import com.vroomvroom.orderservice.infrastructure.dto.CompanyHubDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompanyClientImpl implements CompanyClient {
    @Override
    public CompanyHubDTO getCompanyHubInfo(UUID companyId) {
        // TODO. FeignClient를 통한 API 호출
        return CompanyHubDTO.builder()
                .companyId(companyId)
                .hubId(UUID.randomUUID()) // TODO. 허브 ID 반영 필요
                .build();
    }
}
