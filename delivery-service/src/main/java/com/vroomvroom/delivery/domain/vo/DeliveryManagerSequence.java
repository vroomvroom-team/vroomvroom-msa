package com.vroomvroom.delivery.domain.vo;

import com.vroomvroom.delivery.domain.exception.CustomException;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManagerSequence {

    private Long value;

    private DeliveryManagerSequence(Long value) {
        if (value == null || value < 0 || value > 9) {
            throw new CustomException(DeliveryErrorCode.INVALID_MANAGER_SEQUENCE);
        }
        this.value = value;
    }

    public static DeliveryManagerSequence of(Long value) {
        return new DeliveryManagerSequence(value);
    }

    public DeliveryManagerSequence next() {
        return null;
    }
}
