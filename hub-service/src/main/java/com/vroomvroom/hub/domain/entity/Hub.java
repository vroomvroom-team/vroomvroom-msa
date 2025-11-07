package com.vroomvroom.hub.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.hub.domain.vo.Location;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hub extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID hubId;

    @Column
    private String hubName;

    @Column
    private String address;

    @Embedded
    private Location location;

    public void update(String hubName, String address, BigDecimal latitude, BigDecimal longitude) {
        if (hubName != null) this.hubName = hubName;
        if (address != null) this.address = address;
        if (latitude != null || longitude != null) {
            this.location = new Location(
                    latitude != null ? latitude : this.location.getLatitude(),
                    longitude != null ? longitude : this.location.getLongitude()
            );
        }
    }
}
