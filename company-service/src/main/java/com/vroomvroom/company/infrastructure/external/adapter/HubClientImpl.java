package com.vroomvroom.company.infrastructure.external.adapter;

import com.vroomvroom.company.application.port.HubClient;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.vo.HubId;
import com.vroomvroom.company.infrastructure.external.HubFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubClientImpl implements HubClient {

    private final HubFeignClient hubFeignClient;

    @Override
    public boolean existsHub(HubId hubId) {
        log.info("허브 존재 여부 확인 요청: hubId = {}", hubId);

/*        try {
            return hubFeignClient.existsHub(hubId.getId());
        } catch (Exception e) {
            log.error("[HubClient] 허브 조회 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.HUB_LOOKUP_FAILED);
        }*/

        // TODO. 임시 구현
        return true;
    }
}
