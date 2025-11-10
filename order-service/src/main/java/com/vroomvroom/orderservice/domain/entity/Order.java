package com.vroomvroom.orderservice.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.domain.vo.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
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
    private Long quantity;

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
     * 주문 생성
     */
    public static Order create(
            UUID supplyCompanyId,
            UUID receiveCompanyId,
            UUID supplyHubId,
            UUID receiveHubId,
            UUID productId,
            Money productPrice,
            Long quantity,
            LocalDateTime deadline,
            String requestNote
    ) {
        validateCreateOrder(quantity, deadline, productPrice);

        Money totalPrice = productPrice.multiply(quantity);

        return Order.builder()
                .id(UUID.randomUUID())
                .supplyCompanyId(supplyCompanyId)
                .receiveCompanyId(receiveCompanyId)
                .supplyHubId(supplyHubId)
                .receiveHubId(receiveHubId)
                .productId(productId)
                .totalPrice(totalPrice)
                .quantity(quantity)
                .deadline(deadline)
                .requestNote(requestNote)
                .orderStatus(OrderStatus.PENDING)
                .build();
    }

    /**
     * 주문 유효성 검증
     */
    private static void validateCreateOrder(Long quantity, LocalDateTime deadline, Money productPrice) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다");
        }
        if (deadline == null || deadline.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("납기일은 현재 시간 이후여야 합니다");
        }
        if (productPrice == null || productPrice.isZero()) {
            throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다");
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
     * 삭제 가능 여부 확인
     */
    public boolean isCancellable() {
        return orderStatus.isBeforeShipping();
    }

    /**
     * 수정 가능 여부 확인
     */
    public boolean isModifiable() {
        return orderStatus.isBeforeShipping();
    }

    /**
     * 주문 수정
     */
    public void update(Long quantity, LocalDateTime deadline, String requestNote, Money newTotalPrice) {
        // 수량 변경
        if (quantity != null && !quantity.equals(this.quantity)) {
            this.quantity = quantity;
            this.totalPrice = newTotalPrice;
        }

        // 납기일 변경
        if (deadline != null && !deadline.equals(this.deadline)) {
            this.deadline = deadline;
        }

        // 요청사항 변경
        if (requestNote != null && !requestNote.equals(this.requestNote)) {
            this.requestNote = requestNote;
        }

        // 주문 상태 PENDING으로 변경
        updateStatus(OrderStatus.PENDING);

        this.updatedAt = LocalDateTime.now();
    }
}