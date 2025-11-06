package com.vroomvroom.delivery.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.delivery.domain.vo.ArriveHubId;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerId;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus;
import com.vroomvroom.delivery.domain.vo.DeliverySequence;
import com.vroomvroom.delivery.domain.vo.StartHubId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRoute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "delivery_manager_id", nullable = false))
    private DeliveryManagerId deliveryManagerId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "start_hub_id", nullable = false))
    private StartHubId startHubId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "arrive_hub_id", nullable = false))
    private ArriveHubId arriveHubId;

    private Long expectedDistance;
    private Long expectedDuration;
    private Long actualDistance;
    private Long actualDuration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryRouteStatus status;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sequence", nullable = false))
    private DeliverySequence sequence;
}
