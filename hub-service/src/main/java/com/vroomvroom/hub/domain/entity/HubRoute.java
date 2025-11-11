package com.vroomvroom.hub.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.HubErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub_route")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HubRoute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID routeId;

    @Column
    private String routeName;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_hub_id", nullable = false)
    private Hub departureHub;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_hub_id", nullable = false)
    private Hub arrivalHub;

    @Column
    private Long time;

    @Column
    private Long distance;

    @Column(name = "is_active")
    private Boolean isActive;

    public static HubRoute of(String routeName, Hub departureHub, Hub arrivalHub, Long time, Long distance) {
        validateHubRoute(departureHub, arrivalHub, time, distance);
        return HubRoute.builder()
                .routeName(routeName)
                .departureHub(departureHub)
                .arrivalHub(arrivalHub)
                .time(time)
                .distance(distance)
                .isActive(true)
                .build();
    }

    private static void validateHubRoute(Hub departureHub, Hub arrivalHub, Long time, Long distance) {
        if (departureHub == null || arrivalHub == null) throw new CustomException(HubErrorCode.BAD_REQUEST);
        if (departureHub.getHubId().equals(arrivalHub.getHubId())) throw new CustomException(HubErrorCode.SAME_DEPARTURE_ARRIVAL_HUB);
        if (time == null || time <= 0) throw new CustomException(HubErrorCode.INVALID_TIME);
        if (distance == null || distance <= 0) throw new CustomException(HubErrorCode.INVALID_DISTANCE);
    }

    public void update(String routeName, Long time, Long distance, Boolean isActive) {
        if (time != null && time <= 0) throw new CustomException(HubErrorCode.INVALID_TIME);
        if (distance != null && distance <= 0) throw new CustomException(HubErrorCode.INVALID_DISTANCE);
        if (routeName != null) this.routeName = routeName;
        if (time != null) this.time = time;
        if (distance != null) this.distance = distance;
        if (isActive != null) this.isActive = isActive;
    }
}
