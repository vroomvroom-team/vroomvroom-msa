package com.vroomvroom.delivery.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerSequence;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.domain.vo.HubId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DeliveryManager extends BaseTimeEntity {

    @Id
    @Column(name = "delivery_manager_id")
    private Long id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_id"))
    private HubId hubId;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteManagerAssignment> assignments;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryManagerType type;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sequence"))
    private DeliveryManagerSequence sequence; // manager sequence : 배송담당자 순번 (각자의 회사에서의 순번)

    public static DeliveryManager createHubManager(
        Long userId,
        DeliveryManagerType type,
        DeliveryManagerSequence sequence
    ) {
        return DeliveryManager.builder()
            .id(userId)
            .type(type)
            .hubId(null) // 허브 배송 담당자는 소속 허브가 없으므로 null
            .sequence(sequence)
            .build();
    }

    public static DeliveryManager createCompanyManager(
        Long userId,
        DeliveryManagerType type,
        HubId hubId,
        DeliveryManagerSequence sequence
    ) {
        return DeliveryManager.builder()
            .id(userId)
            .type(type)
            .hubId(hubId)
            .sequence(sequence)
            .build();
    }
}
