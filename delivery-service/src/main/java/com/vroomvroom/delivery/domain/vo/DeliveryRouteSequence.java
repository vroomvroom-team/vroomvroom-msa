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
public class DeliveryRouteSequence {

    private Long value;

    private DeliveryRouteSequence(Long value) {
        if (value < 0) {
            throw new IllegalArgumentException("배송 순번은 0번부터 시작됩니다.");
        }
        this.value = value;
    }

    public static DeliveryRouteSequence of(Long value) {
        return new DeliveryRouteSequence(value);
    }

    public DeliveryRouteSequence next() {
        return new DeliveryRouteSequence(this.value + 1);
    }
}
