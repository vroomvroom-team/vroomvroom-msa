package com.vroomvroom.orderservice.infrastructure.repository;

import com.vroomvroom.orderservice.domain.entity.Order;
import com.vroomvroom.orderservice.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaOrderRepository.findById(id);
    }

    @Override
    public Optional<Order> findByIdAndDeletedAtIsNull(UUID id) {
        return jpaOrderRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Page<Order> findAllByDeletedAtIsNull(Pageable pageable) {
        return jpaOrderRepository.findAllByDeletedAtIsNull(pageable);
    }

    @Override
    public List<Order> findAll() {
        return jpaOrderRepository.findAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaOrderRepository.existsById(id);
    }

    @Override
    public void delete(Order order) {
        jpaOrderRepository.delete(order);
    }
}