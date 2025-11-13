package com.vroomvroom.delivery.infrastructure.external;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.UserClient;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFeignAdapter implements UserClient {

    private final UserFeignClient userFeignClient;

    @Override
    public void verifyUserHasRole(Long userId, DeliveryManagerType deliveryManagerType) {
        if (!userFeignClient.hasRole(userId, deliveryManagerType.name())) {
            throw new CustomException(DeliveryErrorCode.INVALID_MANAGER_TYPE);
        }
    }
}
