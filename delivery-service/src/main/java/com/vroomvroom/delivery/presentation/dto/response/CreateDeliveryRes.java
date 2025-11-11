package com.vroomvroom.delivery.presentation.dto.response;

import com.vroomvroom.delivery.domain.entity.Delivery;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateDeliveryRes {

    private UUID deliveryId;

    public static CreateDeliveryRes from(Delivery delivery) {
        return CreateDeliveryRes.builder()
            .deliveryId(delivery.getId())
            .build();
    }
}
