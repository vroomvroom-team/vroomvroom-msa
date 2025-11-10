package com.vroomvroom.company.infrastructure.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", path = "api/v1/users")
public interface UserFeignClient {

    @GetMapping("{userId}/exists")
    boolean existsUser(@PathVariable Long userId);

    @GetMapping("/me")
    UUID getHubIdByUserId(@PathVariable("userId") Long userId);
}
