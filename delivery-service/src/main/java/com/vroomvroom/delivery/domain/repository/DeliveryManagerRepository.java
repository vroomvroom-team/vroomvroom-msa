package com.vroomvroom.delivery.domain.repository;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository {

    DeliveryManager save(DeliveryManager deliveryManager);

    boolean existsByDeliveryManagerId(Long userId);

    Page<DeliveryManager> findAllDelivery(Pageable pageable);

    Page<DeliveryManager> findAllByType(DeliveryManagerType type, Pageable pageable);

    Optional<DeliveryManager> findDeliveryById(Long id);

    Long nextGlobalSequence();

    Long nextHubSequence(UUID hubId);
}
