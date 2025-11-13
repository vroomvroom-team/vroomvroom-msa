package com.vroomvroom.delivery.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.delivery.application.service.DeliveryRouteService;
import com.vroomvroom.delivery.presentation.api.DeliveryRouteApiDocs;
import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRouteRes;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryRouteController implements DeliveryRouteApiDocs {

    private final DeliveryRouteService deliveryRouteService;

    // 허브 출발
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    @PatchMapping("/{deliveryId}/delivery-routes/{routeId}")
    public ResponseEntity<ApiResponse<Void>> startHub(
        @PathVariable UUID deliveryId,
        @PathVariable UUID routeId
    ) {
        deliveryRouteService.startHub(deliveryId, routeId);
        return ResponseEntity.ok().build();
    }

    // 허브 도착 - 배송/배송경로 상태 업데이트 (허브 -> 허브)
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    @PatchMapping("/{deliveryId}/delivery-routes/{routeId}/status")
    public ResponseEntity<ApiResponse<Void>> arriveHub(
        @PathVariable UUID deliveryId,
        @PathVariable UUID routeId
    ) {
        deliveryRouteService.arriveHub(deliveryId, routeId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping("/{deliveryId}/delivery-routes")
    public ResponseEntity<ApiResponse<List<GetDeliveryRouteRes>>> getDeliveryAllRoute(
        @PathVariable UUID deliveryId
    ) {
        List<GetDeliveryRouteRes> response = deliveryRouteService.getDeliveryAllRoute(deliveryId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
