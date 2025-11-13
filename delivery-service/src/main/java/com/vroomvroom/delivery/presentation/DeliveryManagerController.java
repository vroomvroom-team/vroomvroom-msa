package com.vroomvroom.delivery.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.delivery.application.command.CreateManagerCommand;
import com.vroomvroom.delivery.application.service.DeliveryManagerService;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.presentation.api.DeliveryManagerApiDocs;
import com.vroomvroom.delivery.presentation.dto.request.CreateManagerReq;
import com.vroomvroom.delivery.presentation.dto.response.CreateManagerRes;
import com.vroomvroom.delivery.presentation.dto.response.DeliveryManagerRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class DeliveryManagerController implements DeliveryManagerApiDocs {

    private final DeliveryManagerService deliveryManagerService;

    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    @PostMapping("/manager")
    public ResponseEntity<ApiResponse<CreateManagerRes>> createDeliveryManager(
        @Valid @RequestBody CreateManagerReq request
    ) {
        CreateManagerCommand command = CreateManagerCommand.from(request);
        CreateManagerRes response = deliveryManagerService.createManager(command);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 배송 담당자 전체 조회
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping("/manager")
    public ResponseEntity<ApiResponse<PageResponse<DeliveryManagerRes>>> getAllDeliveryManager(
        @RequestParam(required = false) DeliveryManagerType type,
        Pageable pageable) {
        Page<DeliveryManagerRes> page = deliveryManagerService.getDeliveryManagers(type, pageable);
        PageResponse<DeliveryManagerRes> response = PageResponse.fromPage(page);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 배송 담당자 단건 조회
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping("/manager/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryManagerRes>> getDeliveryManagerById(
        @PathVariable Long deliveryId) {
        DeliveryManagerRes response = deliveryManagerService.getDelivery(deliveryId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasAnyRole('HUB_MANAGER')")
    @DeleteMapping("/manager/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryManagerRes>> deleteDeliveryManagerById(
        @PathVariable Long deliveryId) {
        DeliveryManagerRes response = deliveryManagerService.deleteDelivery(deliveryId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
