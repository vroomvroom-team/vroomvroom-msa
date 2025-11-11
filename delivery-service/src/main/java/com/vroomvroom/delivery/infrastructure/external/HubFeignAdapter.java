package com.vroomvroom.delivery.infrastructure.external;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.application.dto.GetDeliveryRoutesReq;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.HubClient;
import com.vroomvroom.delivery.infrastructure.external.dto.HubRouteDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubFeignAdapter implements HubClient {

    private final HubFeignClient hubFeignClient;

    @Override
    public List<HubRouteDTO> getRoutes(GetDeliveryRoutesReq request) {
        return hubFeignClient.getRoutes(request);
    }

    @Override
    public void verifyExists(UUID hubId) {
        if (!hubFeignClient.exists(hubId)) {
            throw new CustomException(DeliveryErrorCode.HUB_NOT_FOUND);
        }
    }

    @Override
    public List<UUID> getAllHubId() {
        return hubFeignClient.getAllHubId();
    }
}
