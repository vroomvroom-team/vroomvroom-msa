package com.vroomvroom.delivery.application.service;

import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRouteRes;
import java.util.List;
import java.util.UUID;

public interface DeliveryRouteService {

    DeliveryRoute findRouteOrThrow(UUID routeId);

    /**
     * 배송 경로 상태 변경
     */
    void arriveHub(UUID deliveryId, UUID routeId);

    List<GetDeliveryRouteRes> getDeliveryAllRoute(UUID deliveryId);

    void startHub(UUID deliveryId, UUID routeId);

}
