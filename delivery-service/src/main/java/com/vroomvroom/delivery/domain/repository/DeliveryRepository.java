package com.vroomvroom.delivery.domain.repository;

import com.vroomvroom.delivery.domain.entity.Delivery;

public interface DeliveryRepository {

    /**
     * 배송 저장
     * @param delivery 저장할 배송
     * @return 저장된 배송
     */
    Delivery save(Delivery delivery);
}
