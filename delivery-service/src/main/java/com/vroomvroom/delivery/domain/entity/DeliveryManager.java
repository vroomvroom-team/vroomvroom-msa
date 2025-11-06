package com.vroomvroom.delivery.domain.entity;

import com.vroomvroom.delivery.domain.vo.DeliveryId;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.domain.vo.DeliverySequence;
import com.vroomvroom.delivery.domain.vo.HubId;
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
import jakarta.persistence.OneToOne;

@Entity
public class DeliveryManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryManagerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_route_id", nullable = false)
    private DeliveryRoute deliveryRoute;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "delivery_id"))
    private DeliveryId deliveryId;

    // 소속 허브 id
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_id"))
    private HubId hubId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryManagerType type;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sequence", nullable = false))
    private DeliverySequence sequence;
}
