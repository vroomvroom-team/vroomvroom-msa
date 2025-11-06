package com.vroomvroom.delivery.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryAddress {

    private String address;

    private DeliveryAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    public static DeliveryAddress of(String address) {
        return new DeliveryAddress(address);
    }

    private void validateAddress(String address) {
        if(address == null || address.isEmpty()) {
            throw new IllegalArgumentException("배송 주소는 필수입니다.");
        }
    }
}
