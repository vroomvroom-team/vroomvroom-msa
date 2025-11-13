package com.vroomvroom.orderservice.domain.port;

import com.vroomvroom.orderservice.domain.event.OrderCreatedEvent;

import java.util.UUID;

public interface OrderEventPublisher {
    /**
     * 주문 생성 이벤트 발행
     *
     * @param orderId 주문 ID
     * @param event   주문 생성 이벤트
     */
    void publishOrderCreated(UUID orderId, OrderCreatedEvent event);
}
