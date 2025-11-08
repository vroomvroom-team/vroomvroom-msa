package com.vroomvroom.hub.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub_route", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = { "departure_hub_id", "arrival_hub_id" }
        )
})
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HubRoute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_hub_id", nullable = false)
    private Hub departureHub;

    @ManyToOne
    @JoinColumn(name = "arrival_hub_id", nullable = false)
    private Hub arrivalHub;

    @Column
    private Long time;

    @Column
    private Long distance;

    private static HubRoute of(Hub departureHub, Hub arrivalHub, Long time, Long distance) {
        validateHubRoute(departureHub, arrivalHub);
        return HubRoute.builder()
                .departureHub(departureHub)
                .arrivalHub(arrivalHub)
                .time(time)
                .distance(distance)
                .build();
    }

    private static void validateHubRoute(Hub departureHub, Hub arrivalHub) {
        if (departureHub == null || arrivalHub == null) throw new CustomException(ErrorCode.BAD_REQUEST);
        if (departureHub.getHubId().equals(arrivalHub.getHubId())) throw new CustomException(ErrorCode.SAME_DEPARTURE_ARRIVAL_HUB);
    }
}
