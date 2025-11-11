package com.vroomvroom.delivery.domain.port;

import java.util.UUID;

public interface OrderClient {

    void validateForDelivery(UUID orderId);
}
