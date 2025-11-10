package com.vroomvroom.delivery.application.service;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import java.util.UUID;

public interface DeliveryRouteService {

    DeliveryRoute getRouteOrThrow(UUID routeId);

    /**
     * 트랜잭션 커밋 이후에 다음 배송 경로 배정 이벤트 발행하도록 예약
     */
    void scheduleNextAfterCommit(DeliveryRoute route);

    /**
     * 현재 경로 기준으로 다음 배송 경로가 존재하면 매니저 배정 이벤트 발행
     */
    void triggerNextRouteAssignment(DeliveryRoute finalRoute);

    void assignManager(DeliveryRoute route, DeliveryManager manager);
}
