package com.vroomvroom.delivery.infrastructure.external;

import com.vroomvroom.delivery.application.dto.GetDeliveryRoutesReq;
import com.vroomvroom.delivery.infrastructure.external.dto.HubRouteDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hub-service")
public interface HubFeignClient {

    @GetMapping("/api/v1/hubs")
    List<HubRouteDTO> getRoutes(@RequestBody GetDeliveryRoutesReq request);

    @GetMapping("/api/v1/hubs/{hubId}/exists")
    boolean exists(UUID hubId);
}
