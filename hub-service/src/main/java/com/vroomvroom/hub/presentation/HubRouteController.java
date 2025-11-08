package com.vroomvroom.hub.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.HubRouteService;
import com.vroomvroom.hub.application.dto.HubRouteListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hub-routes")
@RequiredArgsConstructor
public class HubRouteController {

    private final HubRouteService hubRouteService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<HubRouteListRes>>> getHubRouteList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageResponse<HubRouteListRes> res = hubRouteService.getHubRouteList(pageable);
        return ResponseEntity.ok(ApiResponse.success(res));
    }
}
