package com.vroomvroom.delivery.domain.port;

import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;

public interface UserClient {

    void verifyUserHasRole(Long userId, DeliveryManagerType deliveryManagerType);
}
