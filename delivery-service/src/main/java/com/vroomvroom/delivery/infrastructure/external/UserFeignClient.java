package com.vroomvroom.delivery.infrastructure.external;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/api/v1/users/{userId}")
    boolean hasRole(
        @PathVariable Long userId,
        @RequestParam("role") String role
    );

    @GetMapping("/api/v1/users/{userId}/slack")
    UUID getUserSlackId(Long receiverId);
}
