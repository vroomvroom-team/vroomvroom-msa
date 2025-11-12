package com.vroomvroom.delivery.application.service;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRouteRes;
import java.util.UUID;

public interface DeliveryRouteService {

    DeliveryRoute findRouteOrThrow(UUID routeId);

    void assignManager(DeliveryRoute route, DeliveryManager manager);

    /**
     * 배송 경로 상태 변경
     */
    void updateDeliveryRouteStatus(UUID deliveryId, UUID routeId);

    void startHub(UUID deliveryId, UUID routeId);
}
