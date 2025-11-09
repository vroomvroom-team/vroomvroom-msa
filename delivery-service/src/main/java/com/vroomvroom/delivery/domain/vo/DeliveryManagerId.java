package com.vroomvroom.delivery.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManagerId {

    private Long id;

    private DeliveryManagerId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("유효하지 않는 배송담당자ID 입니다.");
        }
        this.id = id;
    }

    public static DeliveryManagerId of(Long id) {
        return new DeliveryManagerId(id);
    }
}
