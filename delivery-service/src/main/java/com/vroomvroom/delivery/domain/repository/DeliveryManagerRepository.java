package com.vroomvroom.delivery.domain.repository;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import java.util.UUID;

public interface DeliveryManagerRepository {

    DeliveryManager save(DeliveryManager deliveryManager);

    boolean existsByDeliveryManagerId(Long userId);


    Long nextGlobalSequence();

    Long nextHubSequence(UUID hubId);
}
