package com.vroomvroom.delivery.infrastructure.repository;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.repository.DeliveryManagerRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryManagerRepositoryAdapter implements DeliveryManagerRepository {

    private final JpaDeliveryManagerRepository jpaDeliveryManagerRepository;

    @Override
    public DeliveryManager save(DeliveryManager deliveryManager) {
        return jpaDeliveryManagerRepository.save(deliveryManager);
    }

    @Override
    public boolean existsByDeliveryManagerId(Long userId) {
        return jpaDeliveryManagerRepository.existsById(userId);
    }

    @Override
    public Long nextGlobalSequence() {
        return jpaDeliveryManagerRepository.nextGlobalSequence();
    }

    @Override
    public Long nextHubSequence(UUID hubId) {
        return jpaDeliveryManagerRepository.nextHubSequence(hubId);
    }

}
