package com.vroomvroom.orderservice.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vroomvroom.orderservice.application.dto.OrderDTO;
import com.vroomvroom.orderservice.domain.vo.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class OrderRes {
    private UUID orderId;
    private UUID supplyCompanyId;
    private UUID receiveCompanyId;
    private UUID supplyHubId;
    private UUID receiveHubId;
    private UUID productId;
    private UUID deliveryId;
    private BigDecimal totalPrice;
    private Long quantity;
    private LocalDateTime deadline;
    private String requestNote;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime cancelledAt;

    public static OrderRes from(OrderDTO orderDTO) {
        return OrderRes.builder()
                .orderId(orderDTO.getOrderId())
                .supplyCompanyId(orderDTO.getSupplyCompanyId())
                .receiveCompanyId(orderDTO.getReceiveCompanyId())
                .supplyHubId(orderDTO.getSupplyHubId())
                .receiveHubId(orderDTO.getReceiveHubId())
                .productId(orderDTO.getProductId())
                .deliveryId(orderDTO.getDeliveryId())
                .totalPrice(orderDTO.getTotalPrice().getAmount())
                .quantity(orderDTO.getQuantity())
                .deadline(orderDTO.getDeadline())
                .requestNote(orderDTO.getRequestNote())
                .orderStatus(orderDTO.getOrderStatus())
                .createdAt(orderDTO.getCreatedAt())
                .updatedAt(orderDTO.getUpdatedAt())
                .cancelledAt(orderDTO.getDeletedAt()) // 취소 시점
                .build();
    }
}