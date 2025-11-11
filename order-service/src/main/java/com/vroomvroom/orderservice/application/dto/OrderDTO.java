package com.vroomvroom.orderservice.application.dto;

import com.vroomvroom.orderservice.domain.entity.Order;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.domain.vo.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private UUID orderId;
    private UUID supplyCompanyId;
    private UUID receiveCompanyId;
    private UUID supplyHubId;
    private UUID receiveHubId;
    private UUID productId;
    private UUID deliveryId;
    private Money totalPrice;
    private Long quantity;
    private LocalDateTime deadline;
    private String requestNote;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static OrderDTO from(Order order) {
        return OrderDTO.builder()
                .orderId(order.getId())
                .supplyCompanyId(order.getSupplyCompanyId())
                .receiveCompanyId(order.getReceiveCompanyId())
                .supplyHubId(order.getSupplyHubId())
                .receiveHubId(order.getReceiveHubId())
                .productId(order.getProductId())
                .deliveryId(order.getDeliveryId())
                .totalPrice(order.getTotalPrice())
                .quantity(order.getQuantity())
                .deadline(order.getDeadline())
                .requestNote(order.getRequestNote())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .deletedAt(order.getDeletedAt())
                .build();
    }
}
