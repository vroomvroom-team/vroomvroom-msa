package com.vroomvroom.delivery.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArriveHubId {

    private UUID id;
    private ArriveHubId(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("유효하지 않는 도착허브ID 입니다.");
        }
        this.id = id;
    }

    public static ArriveHubId of(UUID id) {
        return new ArriveHubId(id);
    }
}
