package com.vroomvroom.delivery.infrastructure.external;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderFeignClient {

    @GetMapping("/api/v1/orders/{orderId}/validation")
    ResponseEntity<Void> validateForDelivery(@PathVariable UUID orderId);
}
