package com.vroomvroom.hub.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.HubService;
import com.vroomvroom.hub.application.command.*;
import com.vroomvroom.hub.application.dto.StockRes;
import com.vroomvroom.hub.presentation.dto.request.CreateStockReq;
import com.vroomvroom.hub.presentation.dto.request.DecreaseStockReq;
import com.vroomvroom.hub.presentation.dto.request.UpdateHubReq;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.presentation.dto.request.CreateHubReq;
import com.vroomvroom.hub.presentation.dto.response.CreateStockRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateHubRes>> createHub(@RequestBody CreateHubReq req) {
        CreateHubCommand command = new CreateHubCommand(
                req.getHubName(),
                req.getAddress(),
                req.getLatitude(),
                req.getLongitude()
        );
        CreateHubRes res = hubService.createHub(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<HubListRes>>> getHubList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageResponse<HubListRes> res = hubService.getHubList(pageable);
        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @GetMapping("/{hubId}")
    public ResponseEntity<ApiResponse<HubDetailRes>> getHubDetail(@PathVariable UUID hubId) {
        HubDetailRes res = hubService.getHubDetail(hubId);
        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @PatchMapping("/{hubId}")
    public ResponseEntity<ApiResponse<Void>> updateHub(@PathVariable UUID hubId,
                                                       @RequestBody UpdateHubReq req) {
        UpdateHubCommand command = new UpdateHubCommand(
                req.getHubName(),
                req.getAddress(),
                req.getLatitude(),
                req.getLongitude()
        );
        hubService.updateHub(hubId, command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<ApiResponse<Void>> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{hubId}/exists")
    public boolean existHub(@PathVariable UUID hubId) {
        return hubService.existsHub(hubId);
    }

    @PostMapping("/{hubId}/stocks")
    public ResponseEntity<ApiResponse<CreateStockRes>> createStock(@PathVariable UUID hubId,
                                                                   @RequestBody CreateStockReq req) {
        CreateStockCommand command = new CreateStockCommand(
                hubId,
                req.getProductId(),
                req.getQuantity()
        );
        CreateStockRes res = hubService.createStock(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    @GetMapping("/{hubId}/stocks")
    public ResponseEntity<ApiResponse<List<StockRes>>> getStockList(@PathVariable UUID hubId) {
        List<StockRes> res = hubService.getStockList(hubId);
        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @PutMapping("/{hubId}/stocks/decrease")
    public ResponseEntity<ApiResponse<Void>> updateStock(@PathVariable UUID hubId,
                                                         @RequestBody DecreaseStockReq req) {
        DecreaseStockCommand command = new DecreaseStockCommand(
                hubId, req.getProductId(), req.getQuantity(), req.getOrderId()
        );
        hubService.decreaseStock(command);
        return ResponseEntity.noContent().build();
    }
}