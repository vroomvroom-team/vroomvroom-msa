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
public class DeliveryManagerId {

    private UUID id;

    private DeliveryManagerId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("유효하지 않는 배송담당자ID 입니다.");
        }
        this.id = id;
    }

    public static DeliveryManagerId of(UUID id) {
        return new DeliveryManagerId(id);
    }
}
