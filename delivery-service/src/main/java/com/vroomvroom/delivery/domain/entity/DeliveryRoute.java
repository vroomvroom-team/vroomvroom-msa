package com.vroomvroom.delivery.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.delivery.domain.vo.ArriveHubId;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerId;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteSequence;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus;
import com.vroomvroom.delivery.domain.vo.StartHubId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DeliveryRoute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "delivery_manager_id", nullable = false))
    private DeliveryManagerId deliveryManagerId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "start_hub_id", nullable = false))
    private StartHubId startHubId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "arrive_hub_id", nullable = false))
    private ArriveHubId arriveHubId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteManagerAssignment> assignments;

    private Long expectedDistance; // 미터(m)
    private Long expectedDuration; // 초(s)

    private Long actualDistance; // 미터(m)
    private Long actualDuration; // 초(s)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryRouteStatus status;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sequence", nullable = false))
    private DeliveryRouteSequence sequence; // route sequence : 배송 순번

    public static DeliveryRoute create(
        DeliveryRouteStatus deliveryRouteStatus,
        DeliveryRouteSequence deliverySequence,
        StartHubId startHubId, ArriveHubId arriveHubId
    ) {
        return DeliveryRoute.builder()
            .status(deliveryRouteStatus)
            .sequence(deliverySequence)
            .startHubId(startHubId)
            .arriveHubId(arriveHubId)
            .build();
    }
}
