package com.vroomvroom.delivery.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.delivery.application.command.CompleteDeliveryCommand;
import com.vroomvroom.delivery.application.command.CreateDeliveryCommand;
import com.vroomvroom.delivery.application.service.DeliveryService;
import com.vroomvroom.delivery.presentation.api.DeliveryApiDocs;
import com.vroomvroom.delivery.presentation.dto.request.CreateDeliveryReq;
import com.vroomvroom.delivery.presentation.dto.response.CreateDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.GetAllDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRes;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryApiDocs {

    private final DeliveryService deliveryService;

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping
    public ResponseEntity<ApiResponse<CreateDeliveryRes>> createDelivery(
        @Valid @RequestBody CreateDeliveryReq request
    ) {
        CreateDeliveryCommand command = CreateDeliveryCommand.from(request);
        CreateDeliveryRes response = deliveryService.createDelivery(command);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 허브 -> 업체
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    @PatchMapping("/{deliveryId}/company")
    public ResponseEntity<ApiResponse<Void>> handoffToCompany(
        @PathVariable UUID deliveryId
    ) {
        deliveryService.handoffToCompany(deliveryId);
        return ResponseEntity.ok().build();
    }

    // 배송완료
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    @PostMapping("/{deliveryId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeDelivery(
        @PathVariable UUID deliveryId,
        @RequestHeader("X-User-Id") Long userId,
        @RequestHeader("X-User-Role") String userRole
    ) {
        CompleteDeliveryCommand command = CompleteDeliveryCommand.of(userId, userRole);
        deliveryService.completeDelivery(deliveryId, command);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<GetAllDeliveryRes>>> getAllDelivery(
        @SortDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        PageResponse<GetAllDeliveryRes> response = deliveryService.getAllDelivery(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER', 'COMPANY_MANAGER')")
    @GetMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse<GetDeliveryRes>> getDelivery(
        @PathVariable UUID deliveryId
    ) {
        GetDeliveryRes response = deliveryService.getDelivery(deliveryId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER')")
    @PatchMapping("{deliveryId}")
    public ResponseEntity<ApiResponse<Void>> cancelDelivery(
        @PathVariable UUID deliveryId
    ) {
        deliveryService.cancelDelivery(deliveryId);
        return ResponseEntity.ok().build();
    }

}
