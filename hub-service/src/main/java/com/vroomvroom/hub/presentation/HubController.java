package com.vroomvroom.hub.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.hub.application.HubService;
import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.application.dto.HubRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<ApiResponse<HubRes.CreateHubRes>> createHub(@RequestBody CreateHubCommand command) {
        HubRes.CreateHubRes res = hubService.createHub(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }
}
