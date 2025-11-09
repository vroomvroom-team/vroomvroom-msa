package com.vroomvroom.orderservice.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.domain.vo.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_order")
public class Order extends BaseTimeEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, columnDefinition = "UUID")
    private UUID supplyCompanyId;

    @Column(nullable = false, columnDefinition = "UUID")
    private UUID receiveCompanyId;

    @Column(nullable = false, columnDefinition = "UUID")
    private UUID supplyHubId;

    @Column(nullable = false, columnDefinition = "UUID")
    private UUID receiveHubId;

    @Column(nullable = false, columnDefinition = "UUID")
    private UUID productId;

    @Column(columnDefinition = "UUID")
    private UUID deliveryId;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total_price"))
    private Money totalPrice;

    @Column(nullable = false)
    private BigInteger quantity;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(length = 500)
    private String requestNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OrderStatus orderStatus;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.orderStatus == null) {
            this.orderStatus = OrderStatus.PENDING;
        }
    }

    /**
     * 주문 상태 변경
     */
    public void updateStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }

    /**
     * 배송 ID 할당
     */
    public void assignDelivery(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }

    /**
     * 주문 금액 계산
     */
    public Money calculateTotalPrice() {
        return totalPrice.multiply(quantity.intValue());
    }

    /**
     * 삭제 가능 여부 확인
     */
    public boolean isCancellable() {
        return orderStatus.isBeforeShipping();
    }
}