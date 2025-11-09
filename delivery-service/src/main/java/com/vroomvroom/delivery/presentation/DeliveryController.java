package com.vroomvroom.delivery.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.delivery.application.service.DeliveryManagerService;
import com.vroomvroom.delivery.application.command.CreateManagerCommand;
import com.vroomvroom.delivery.presentation.dto.request.CreateManagerReq;
import com.vroomvroom.delivery.presentation.dto.response.CreateManagerRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
