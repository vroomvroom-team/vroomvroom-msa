package com.vroomvroom.hub.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.hub.domain.vo.ProductId;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_stock")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Stock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID stockId;

    @Embedded
    private ProductId productId;

    @ManyToOne
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @Column
    private Long quantity;

    public static Stock of(ProductId productId, Hub hub, Long quantity) {
        return Stock.builder()
                .productId(productId)
                .hub(hub)
                .quantity(quantity)
                .build();
    }

    public void increase(Long quantity) {
        this.quantity += quantity;
    }
    public void decrease(Long quantity) {
        if (this.quantity < quantity) throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        this.quantity -= quantity;
    }
}