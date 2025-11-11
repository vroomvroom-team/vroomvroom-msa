package com.vroomvroom.hub.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.hub.domain.vo.Location;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.HubErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hub extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID hubId;

    @Column
    private String hubName;

    @Column
    private String address;

    @Column
    private Long hubManagerId;

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "departureHub", cascade = CascadeType.ALL)
    private List<HubRoute> departureRoutes = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalHub", cascade = CascadeType.ALL)
    private List<HubRoute> arrivalRoutes = new ArrayList<>();

    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL)
    private List<Stock> stocks = new ArrayList<>();

    public static Hub of(String hubName, String address, BigDecimal latitude, BigDecimal longitude, Long hubManagerId) {
        validateHub(hubName, address, latitude, longitude, hubManagerId);
        return Hub.builder()
                .hubName(hubName)
                .address(address)
                .location(Location.of(latitude, longitude))
                .hubManagerId(hubManagerId)
                .build();
    }

    private static void validateHub(String hubName, String address, BigDecimal latitude, BigDecimal longitude, Long hubManagerId) {
        if (hubName == null || hubName.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                latitude == null || longitude == null || hubManagerId == null) throw new CustomException(HubErrorCode.BAD_REQUEST);
    }

    public void update(String hubName, String address, BigDecimal latitude, BigDecimal longitude, Long hubManagerId) {
        if (hubName != null) this.hubName = hubName;
        if (address != null) this.address = address;
        if (latitude != null || longitude != null) {
            BigDecimal newLatitude = latitude != null ? latitude : this.location.getLatitude();
            BigDecimal newLongitude = longitude != null ? longitude : this.location.getLongitude();
            this.location = Location.of(newLatitude, newLongitude);
        }
        if (hubManagerId != null) this.hubManagerId = hubManagerId;
    }

    public void delete() {
        this.markAsDeleted();
    }

    public void createRoute(HubRoute route) {
        route.setDepartureHub(this);
        this.departureRoutes.add(route);
    }

    public void removeRoute(HubRoute route) {
        this.departureRoutes.remove(route);
        route.setDepartureHub(null);
        route.markAsDeleted();
    }

    public void createStock(Stock stock) {
        this.stocks.add(stock);
    }
}