package com.vroomvroom.delivery.domain.vo;

import com.vroomvroom.delivery.domain.entity.Delivery;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor
public class DeliveryId {

    private UUID id;
    private DeliveryId(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("유효하지 않는 배송ID 입니다.");
        }
        this.id = id;
    }

    public static DeliveryId of(UUID id) {
        return new DeliveryId(id);
    }
}
