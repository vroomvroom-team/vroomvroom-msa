package com.vroomvroom.delivery.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.delivery.application.command.CreateDeliveryCommand;
import com.vroomvroom.delivery.application.command.CreateManagerCommand;
import com.vroomvroom.delivery.application.service.DeliveryManagerService;
import com.vroomvroom.delivery.application.service.DeliveryRouteService;
import com.vroomvroom.delivery.application.service.DeliveryService;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.presentation.dto.request.CreateDeliveryReq;
import com.vroomvroom.delivery.presentation.dto.request.CreateManagerReq;
import com.vroomvroom.delivery.presentation.dto.response.CreateDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.CreateManagerRes;
import com.vroomvroom.delivery.presentation.dto.response.DeliveryManagerRes;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryManagerService deliveryManagerService;
    private final DeliveryService deliveryService;
    private final DeliveryRouteService deliveryRouteService;

    @PostMapping("/manager")
    public ResponseEntity<ApiResponse<CreateManagerRes>> createDeliveryManager(
        @Valid @RequestBody CreateManagerReq request
    ) {
        CreateManagerCommand command = CreateManagerCommand.from(request);
        CreateManagerRes response = deliveryManagerService.createManager(command);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateDeliveryRes>> createDelivery(
        @Valid @RequestBody CreateDeliveryReq request
    ) {
        CreateDeliveryCommand command = CreateDeliveryCommand.from(request);
        CreateDeliveryRes response = deliveryService.createDelivery(command);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 배송/배송경로 상태 업데이트 (허브 -> 허브)
    @PatchMapping("/{deliveryId}/delivery-routes/{routeId}/status")
    public ResponseEntity<ApiResponse<Void>> updateDeliveryRouteStatus(
        @PathVariable UUID deliveryId,
        @PathVariable UUID routeId
    ) {
        deliveryRouteService.updateDeliveryRouteStatus(deliveryId, routeId);
        return ResponseEntity.ok().build();
    }

    // 배송 담당자 전체 조회
    @GetMapping("/manager")
    public ResponseEntity<ApiResponse<PageResponse<DeliveryManagerRes>>> getAllDeliveryManager(
        @RequestParam(required = false) DeliveryManagerType type,
        Pageable pageable) {
        Page<DeliveryManagerRes> page = deliveryManagerService.getDeliveryManagers(type, pageable);
        PageResponse<DeliveryManagerRes> response = PageResponse.fromPage(page);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 배송 담당자 단건 조회
    @GetMapping("/manager/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryManagerRes>> getDeliveryManagerById(
        @PathVariable Long deliveryId) {
        DeliveryManagerRes response = deliveryManagerService.getDelivery(deliveryId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/manager/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryManagerRes>> deleteDeliveryManagerById(
        @PathVariable Long deliveryId) {
        DeliveryManagerRes response = deliveryManagerService.deleteDelivery(deliveryId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
