package com.vroomvroom.delivery.domain.port;

import com.vroomvroom.delivery.application.dto.GetDeliveryRoutesReq;
import com.vroomvroom.delivery.infrastructure.external.dto.HubRouteDTO;
import java.util.List;
import java.util.UUID;

public interface HubClient {

    /**
     * 공급/수령 업체 허브의 ID를 전달해 각 배송경로에 대한 허브 출발/도착 ID를 요청하는 메서드
     *
     * @return HubDTO : 각 배송경로에 대한 출발/도착 허브ID, 소요시간, 거리를 필드로 가짐.
     */
    List<HubRouteDTO> getRoutes(GetDeliveryRoutesReq request);

    void verifyExists(UUID hubId);
}
