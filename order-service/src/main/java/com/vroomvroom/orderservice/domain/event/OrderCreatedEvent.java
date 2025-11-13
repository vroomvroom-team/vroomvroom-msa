package com.vroomvroom.orderservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 주문 생성 이벤트
 * 배송 서비스로 전달되는 이벤트 페이로드
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private UUID orderId;
    private UUID supplyHubId;
    private UUID receiveHubId;

    /**
     * 주문 생성 이벤트 팩토리 메서드
     */
    public static OrderCreatedEvent from(
            UUID orderId,
            UUID supplyHubId,
            UUID receiveHubId
    ) {
        return new OrderCreatedEvent(
                orderId,
                supplyHubId,
                receiveHubId
        );
    }
}
