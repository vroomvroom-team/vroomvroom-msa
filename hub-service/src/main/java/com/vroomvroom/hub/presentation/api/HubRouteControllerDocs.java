package com.vroomvroom.hub.presentation.api;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.dto.HubRouteDetailRes;
import com.vroomvroom.hub.application.dto.HubRouteListRes;
import com.vroomvroom.hub.application.dto.OptimalRouteRes;
import com.vroomvroom.hub.domain.service.OptimalRouteType;
import com.vroomvroom.hub.presentation.dto.request.CreateHubRouteReq;
import com.vroomvroom.hub.presentation.dto.request.UpdateHubRouteReq;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRouteRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Hub Route API", description = "허브 경로 API")
public interface HubRouteControllerDocs {

    @Operation(summary = "허브 경로 생성", description = "새로운 허브 경로를 생성합니다.")
    ResponseEntity<ApiResponse<CreateHubRouteRes>> createHubRoute(@RequestBody CreateHubRouteReq req);

    @Operation(summary = "허브 경로 목록 조회", description = "허브 경로의 전체 목록을 조회합니다. 페이지 크기: 10, 생성순 기본")
    ResponseEntity<ApiResponse<PageResponse<HubRouteListRes>>> getHubRouteList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);

    @Operation(summary = "허브 경로 상세 조회", description = "허브 경로 ID를 활용해 특정 허브 경로를 상세 조회합니다.")
    @Parameter(name = "routeId", description = "조회할 허브 경로 ID", required = true)
    ResponseEntity<ApiResponse<HubRouteDetailRes>> getHubRouteDetail(@PathVariable UUID routeId);

    @Operation(summary = "허브 경로 수정", description = "허브 경로 ID를 활용해 특정 허브 경로를 수정합니다.")
    @Parameter(name = "routeId", description = "조회할 허브 경로 ID", required = true)
    ResponseEntity<ApiResponse<Void>> updateHubRoute(@PathVariable UUID routeId, @RequestBody UpdateHubRouteReq req);

    @Operation(summary = "허브 경로 삭제", description = "허브 경로 ID를 활용해 특정 허브 경로를 삭제합니다.")
    @Parameter(name = "routeId", description = "조회할 허브 경로 ID", required = true)
    ResponseEntity<ApiResponse<Void>> deleteHubRoute(@PathVariable UUID routeId);

    @Operation(summary = "최단 경로 조회", description = "출발 허브 ID와 도착 허브 ID를 활용해 최단 경로를 찾습니다. 이동 거리(기본)/소요 시간")
    @Parameters({
            @Parameter(name = "departureId", description = "출발 허브 ID", required = true),
            @Parameter(name = "arrivalId", description = "도착 허브 ID", required = true),
            @Parameter(name = "type", description = "기준: 이동 거리/소요 시간")
    })
    ResponseEntity<ApiResponse<OptimalRouteRes>> findOptimalPathByDistance(@RequestParam UUID departureId,
                                                                           @RequestParam UUID arrivalId,
                                                                           @RequestParam(defaultValue = "DISTANCE") OptimalRouteType type);
}
