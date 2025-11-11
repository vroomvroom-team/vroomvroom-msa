package com.vroomvroom.delivery.infrastructure.repository;

import com.vroomvroom.delivery.domain.entity.Delivery;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryRepositoryAdapter implements DeliveryRepository {

    private final JpaDeliveryRepository jpaDeliveryRepository;

    @Override
    public Delivery save(Delivery delivery) {
        return jpaDeliveryRepository.save(delivery);
    }

    @Override
    public Optional<DeliveryRoute> findByRouteId(UUID routeId) {
        return jpaDeliveryRepository.findByRouteId(routeId);
    }

    @Override
    public Optional<DeliveryRoute> findByDeliveryIdAndSequence(
        UUID deliveryId, Long nextSequence) {
        return jpaDeliveryRepository.findByDeliveryIdAndSequence(deliveryId, nextSequence);
    }

    @Override
    public Page<UnassignedRouteIds> findUnassignedRouteKeys(Pageable pageable) {
        return jpaDeliveryRepository.findUnassignedRouteKeys(pageable);
    }
}
