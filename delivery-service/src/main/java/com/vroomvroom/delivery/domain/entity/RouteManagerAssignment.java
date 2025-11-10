package com.vroomvroom.delivery.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_route_manager_assignment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteManagerAssignment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_route_id", nullable = false)
    private DeliveryRoute route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devliery_manager_id", nullable = false)
    private DeliveryManager manager; // DeliveryManager 중에서도 타입이 HUB_MANAGER

    private RouteManagerAssignment(DeliveryRoute route, DeliveryManager manager) {
        this.route = route;
        this.manager = manager;
    }

    public static RouteManagerAssignment create(DeliveryRoute route, DeliveryManager manager) {
        RouteManagerAssignment managerAssignment = new RouteManagerAssignment(route, manager);
        manager.getAssignments().add(managerAssignment);
        route.getAssignments().add(managerAssignment);
        return managerAssignment;
    }
}
