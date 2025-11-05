package com.vroomvroom.orderservice.infrastructure.repository;

import com.vroomvroom.orderservice.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByIdAndDeletedAtIsNull(UUID id);

    Page<Order> findAllByDeletedAtIsNull(Pageable pageable);
}