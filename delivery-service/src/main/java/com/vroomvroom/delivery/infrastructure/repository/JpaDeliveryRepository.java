package com.vroomvroom.delivery.infrastructure.repository;

import com.vroomvroom.delivery.domain.entity.Delivery;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID> {

}
