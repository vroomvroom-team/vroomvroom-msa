package com.vroomvroom.hub.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.HubService;
import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.application.command.UpdateHubCommand;
import com.vroomvroom.hub.presentation.dto.request.UpdateHubReq;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.presentation.dto.request.CreateHubReq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
            ) {
        PageResponse<HubListRes> res = hubService.getHubList(page, size, direction);
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
}
