package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.orderservice.application.service.CompanyClient;
import com.vroomvroom.orderservice.exception.OrderErrorCode;
import com.vroomvroom.orderservice.infrastructure.dto.CompanyHubDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyClientImpl implements CompanyClient {

    private final CompanyFeignClient companyFeignClient;

    @Override
    public CompanyHubDTO getCompanyHubInfo(UUID companyId) {
        log.info("업체 정보 조회 요청 : companyId = {}", companyId);

        // TODO. EUREKA Server 적용 필요
        /*
        try {
            ApiResponse<CompanyHubDTO> response = companyFeignClient.getCompanyHubInfo(companyId);

            if (!response.isSuccess() || response.getData() == null) {
                log.error("업체 정보 조회 실패 : companyId = {}", companyId);
                throw new CustomException(OrderErrorCode.COMPANY_NOT_FOUND);
            }

            log.info("업체 허브 정보 조회 성공: companyId={}, hubId={}",
                    companyId, response.getData().getHubId());

            return response.getData();
        } catch (FeignException.NotFound e) {
            log.error("업체를 찾을 수 없음 : companyId = {}", companyId);
            throw new CustomException(OrderErrorCode.COMPANY_NOT_FOUND);
        } catch (FeignException e) {
            log.error("업체 서비스 호출 실패 : status={}, message={}", e.status(), e.getMessage());
            throw new CustomException(OrderErrorCode.COMPANY_SERVICE_UNAVAILABLE);
        }
         */

        // TODO. EUREKA Server 적용 시 삭제
        return CompanyHubDTO.builder()
                .companyId(companyId)
                .hubId(UUID.randomUUID()) // TODO. 허브 ID 반영 필요
                .build();
    }
}
