package com.vroomvroom.company.infrastructure.external.adapter;

import com.vroomvroom.company.application.port.HubClient;
import com.vroomvroom.company.infrastructure.external.HubFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubClientImpl implements HubClient {

    private final HubFeignClient hubFeignClient;

    @Override
    public boolean existsHub(UUID hubId) {
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

    @Override
    public boolean existsHubManager(UUID hubId, Long userId) {
        log.info("허브 매니저 확인 요청: hubId={}, userId={}", hubId, userId);

/*        try {
            return hubFeignClient.isHubManager(hubId, userId);
        } catch (Exception e) {
            log.error("[HubClient] 허브 매니저 조회 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.HUB_MANAGER_LOOKUP_FAILED);
        }*/

        // TODO. 임시 구현
        return true;
    }
}
