package com.vroomvroom.delivery.infrastructure.external;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.UserClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFeignAdapter implements UserClient {

    private final UserFeignClient userFeignClient;

    @Override
    public void verifyUserHasRole(Long userId, String role) {
        if ("COMPANY_MANAGER".equals(role)) {
            role = "DELIVERY_MANAGER";
        }
        if (!userFeignClient.hasRole(userId, role)) {
            throw new CustomException(DeliveryErrorCode.INVALID_MANAGER_TYPE);
        }
    }

    @Override
    public UUID getUserSlackId(Long receiverId) {
        return userFeignClient.getUserSlackId(receiverId);
    }
}
