package com.vroomvroom.delivery.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.delivery.application.service.DeliveryManagerService;
import com.vroomvroom.delivery.application.command.CreateManagerCommand;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.presentation.dto.request.CreateManagerReq;
import com.vroomvroom.delivery.presentation.dto.response.CreateManagerRes;
import com.vroomvroom.delivery.presentation.dto.response.DeliveryManagerRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryManagerService deliveryManagerService;

    @PostMapping("/manager")
    public ResponseEntity<ApiResponse<CreateManagerRes>> createDeliveryManager(
        @Valid @RequestBody CreateManagerReq request
    ) {
        CreateManagerCommand command = new CreateManagerCommand(
            request.getUserId(),
            request.getHubId(),
            request.getType()
        );
        CreateManagerRes response = deliveryManagerService.createManager(command);

        return ResponseEntity.ok(ApiResponse.success(response));
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
    public ResponseEntity<ApiResponse<DeliveryManagerRes>> getDeliveryManagerById(@PathVariable Long deliveryId) {
        DeliveryManagerRes response = deliveryManagerService.getDelivery(deliveryId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/manager/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryManagerRes>> deleteDeliveryManagerById(@PathVariable Long deliveryId) {
        DeliveryManagerRes response = deliveryManagerService.deleteDelivery(deliveryId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
