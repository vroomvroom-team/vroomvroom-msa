package com.vroomvroom.orderservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private UUID supplyCompanyId;
    private UUID receiveCompanyId;
    private UUID supplyHubId;
    private UUID receiveHubId;
    private UUID productId;
    private BigDecimal totalPrice;
    private Long quantity;
    private LocalDateTime deadline;
    private String requestNote;

    /**
     * 주문 생성 이벤트 팩토리 메서드
     */
    public static OrderCreatedEvent from(
            UUID orderId,
            UUID supplyCompanyId,
            UUID receiveCompanyId,
            UUID supplyHubId,
            UUID receiveHubId,
            UUID productId,
            BigDecimal totalPrice,
            Long quantity,
            LocalDateTime deadline,
            String requestNote
    ) {
        return new OrderCreatedEvent(
                orderId,
                supplyCompanyId,
                receiveCompanyId,
                supplyHubId,
                receiveHubId,
                productId,
                totalPrice,
                quantity,
                deadline,
                requestNote
        );
    }
}
