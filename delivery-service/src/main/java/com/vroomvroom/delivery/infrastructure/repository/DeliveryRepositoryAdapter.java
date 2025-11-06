package com.vroomvroom.delivery.infrastructure.repository;

import com.vroomvroom.delivery.domain.entity.Delivery;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryRepositoryAdapter implements DeliveryRepository {

    private final JpaDeliveryRepository jpaDeliveryRepository;

    @Override
    public Delivery save(Delivery delivery) {
        return jpaDeliveryRepository.save(delivery);
    }
}
