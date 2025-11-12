package com.vroomvroom.delivery.domain.repository;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryManagerRepository {

    DeliveryManager save(DeliveryManager deliveryManager);

    boolean existsByDeliveryManagerId(Long userId);

    Page<DeliveryManager> findAllDelivery(Pageable pageable);

    Page<DeliveryManager> findAllByType(DeliveryManagerType type, Pageable pageable);

    Optional<DeliveryManager> findDeliveryById(Long id);

    Long nextGlobalSequence();

    Long nextHubSequence(UUID hubId);

    Optional<DeliveryManager> findById(Long userId);

    Optional<DeliveryManager> findAvailableManagerForAssignment(
        Long sequence, DeliveryManagerType deliveryManagerType, UUID hubId);

    Optional<DeliveryManager> findBySequenceAndType(Long sequence,
        DeliveryManagerType deliveryManagerType);
}
