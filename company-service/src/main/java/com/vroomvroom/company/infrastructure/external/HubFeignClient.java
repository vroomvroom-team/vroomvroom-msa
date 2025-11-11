package com.vroomvroom.company.infrastructure.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service", path = "api/v1/hubs")
public interface HubFeignClient {

    @GetMapping("{hubId}/exists")
    boolean existsHub(@PathVariable UUID hubId);

    @GetMapping("/{hubId}/managers/{userId}/check")
    boolean isHubManager(
            @PathVariable("hubId") UUID hubId,
            @PathVariable("userId") Long userId
    );
}
