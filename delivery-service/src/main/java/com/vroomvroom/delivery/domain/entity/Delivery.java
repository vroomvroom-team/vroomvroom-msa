package com.vroomvroom.delivery.domain.entity;

import com.vroomvroom.delivery.domain.vo.ArriveHubId;
import com.vroomvroom.delivery.domain.vo.DeliveryAddress;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerId;
import com.vroomvroom.delivery.domain.vo.DeliveryStatus;
import com.vroomvroom.delivery.domain.vo.OrderId;
import com.vroomvroom.delivery.domain.vo.ReceiverId;
import com.vroomvroom.delivery.domain.vo.ReceiverSlackId;
import com.vroomvroom.delivery.domain.vo.StartHubId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "p_delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends AbstractAggregateRoot<Delivery> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "order_id", nullable = false))
    private OrderId orderId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "delivery_manager_id", nullable = false))
    private DeliveryManagerId deliveryManagerId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "start_hub_id", nullable = false))
    private StartHubId startHubId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "arrive_hub_id", nullable = false))
    private ArriveHubId arriveHubId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "receiver_id", nullable = false))
    private ReceiverId receiverId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "receiver_slack_id", nullable = false))
    private ReceiverSlackId receiverSlackId;

    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(name = "arrive_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime arriveTime;

    @Embedded
    @AttributeOverride(name = "address", column = @Column(name = "delivery_address", nullable = false))
    private DeliveryAddress deliveryAddress;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
