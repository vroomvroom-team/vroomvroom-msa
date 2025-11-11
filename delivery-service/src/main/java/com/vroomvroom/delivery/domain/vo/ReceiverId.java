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
public class ReceiverId {

    private Long id;

    private ReceiverId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("유효하지 않는 수령인ID 입니다.");
        }
        this.id = id;
    }

    public static ReceiverId of(Long id) {
        return new ReceiverId(id);
    }
}
