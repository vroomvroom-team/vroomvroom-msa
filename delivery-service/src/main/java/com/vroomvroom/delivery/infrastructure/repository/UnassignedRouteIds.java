package com.vroomvroom.delivery.infrastructure.repository;

import java.util.UUID;

public interface UnassignedRouteIds {

    UUID getRouteId();

    UUID getDeliveryId();
}
