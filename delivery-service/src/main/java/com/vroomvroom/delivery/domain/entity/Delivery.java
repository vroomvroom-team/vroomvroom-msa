package com.vroomvroom.delivery.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.delivery.domain.vo.ArriveHubId;
import com.vroomvroom.delivery.domain.vo.DeliveryAddress;
import com.vroomvroom.delivery.domain.vo.DeliveryStatus;
import com.vroomvroom.delivery.domain.vo.OrderId;
import com.vroomvroom.delivery.domain.vo.ReceiverId;
import com.vroomvroom.delivery.domain.vo.ReceiverSlackId;
import com.vroomvroom.delivery.domain.vo.StartHubId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Delivery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "order_id", nullable = false, unique = true))
    private OrderId orderId;

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

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

    @Column(name = "start_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(name = "arrive_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime arriveTime;

    @Embedded
    @AttributeOverride(name = "address", column = @Column(name = "delivery_address", nullable = false))
    private DeliveryAddress deliveryAddress;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public static Delivery create(
        OrderId orderId,
        StartHubId startHubId, ArriveHubId arriveHubId,
        List<DeliveryRoute> deliveryRoutes,
        DeliveryAddress address,
        ReceiverId receiverId, ReceiverSlackId receiverSlackId
    ) {
        return Delivery.builder()
            .orderId(orderId)
            .startHubId(startHubId)
            .arriveHubId(arriveHubId)
            .receiverId(receiverId)
            .receiverSlackId(receiverSlackId)
            .deliveryRoutes(deliveryRoutes)
            .deliveryAddress(address)
            .status(DeliveryStatus.HUB_WAITING)
            .build();
    }

    public Optional<DeliveryRoute> findFirstRoute() {
        return this.getDeliveryRoutes().stream().findFirst();
    }

    public void updateStartTime() {
        this.startTime = LocalDateTime.now();
    }

    public void updateStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void updateArriveTime() {
        this.arriveTime = LocalDateTime.now();
    }
}
