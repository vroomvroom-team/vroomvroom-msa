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
public class DeliverySequence {

    private int value;

    private DeliverySequence(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("배송 순번은 0번부터 시작됩니다.");
        }
        this.value = value;
    }

    public static DeliverySequence of(int value) {
        return new DeliverySequence(value);
    }

    public DeliverySequence next() {
        return new DeliverySequence(this.value + 1);
    }
}
