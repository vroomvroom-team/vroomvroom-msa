package com.vroomvroom.delivery.presentation.dto.response;

import com.vroomvroom.delivery.domain.entity.Delivery;
import java.util.UUID;
import lombok.Getter;

@Getter
public class GetAllDeliveryRes {

    private UUID deliveryId;
    private String address;
    private String status;

    private GetAllDeliveryRes(UUID id, String address, String status) {
        this.deliveryId = id;
        this.address = address;
        this.status = status;
    }

    public static GetAllDeliveryRes from(Delivery delivery) {
        return new GetAllDeliveryRes(
            delivery.getId(),
            delivery.getDeliveryAddress().getAddress(),
            delivery.getStatus().name()
        );
    }
}
