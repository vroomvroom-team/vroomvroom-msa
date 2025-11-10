package com.vroomvroom.delivery.infrastructure.external;

import com.vroomvroom.delivery.domain.exception.CustomException;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.OrderClient;
import feign.FeignException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFeignAdapter implements OrderClient {

    private final OrderFeignClient orderFeignClient;

    @Override
    public void validateForDelivery(UUID orderId) {
        try {
            orderFeignClient.validateForDelivery(orderId);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new CustomException(DeliveryErrorCode.ORDER_NOT_FOUND);
            } else if (e.status() == 409) {
                throw new CustomException(DeliveryErrorCode.ORDER_STATE_CONFLICT);
            }
        }
    }
}
