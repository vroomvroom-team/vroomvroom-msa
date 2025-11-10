package com.vroomvroom.delivery.infrastructure.repository;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.repository.DeliveryManagerRepository;

import java.util.Optional;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import java.util.Optional;
import java.util.UUID;

import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<DeliveryManager> findAllDelivery(Pageable pageable) {
        return jpaDeliveryManagerRepository.findAllByDeletedAtIsNull(pageable);
    }

    @Override
    public Page<DeliveryManager> findAllByType(DeliveryManagerType type, Pageable pageable) {
        return jpaDeliveryManagerRepository.findAllByTypeAndDeletedAtIsNull(type, pageable);
    }

    @Override
    public Optional<DeliveryManager> findDeliveryById(Long id) {
        return jpaDeliveryManagerRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Long nextGlobalSequence() {
        return jpaDeliveryManagerRepository.nextGlobalSequence();
    }

    @Override
    public Long nextHubSequence(UUID hubId) {
        return jpaDeliveryManagerRepository.nextHubSequence(hubId);
    }

    @Override
    public Optional<DeliveryManager> findBySequenceAndTypeAndIsActiveFalse(
        Long sequenceToAssign, DeliveryManagerType type, boolean isActive) {
        return jpaDeliveryManagerRepository
            .findBySequenceAndTypeAndIsActiveFalse(sequenceToAssign, type, isActive);
    }
}
