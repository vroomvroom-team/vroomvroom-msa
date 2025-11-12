package com.vroomvroom.hub.presentation.api;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.application.dto.HubManagerRes;
import com.vroomvroom.hub.application.dto.StockRes;
import com.vroomvroom.hub.presentation.dto.request.CreateHubReq;
import com.vroomvroom.hub.presentation.dto.request.CreateStockReq;
import com.vroomvroom.hub.presentation.dto.request.UpdateHubReq;
import com.vroomvroom.hub.presentation.dto.request.UpdateStockReq;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import com.vroomvroom.hub.presentation.dto.response.CreateStockRes;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "Hub API", description = "허브 API")
public interface HubControllerDocs {
    @Operation(summary = "허브 생성", description = "새로운 허브를 생성합니다.")
    ResponseEntity<ApiResponse<CreateHubRes>> createHub(@RequestBody CreateHubReq req);

    @Operation(summary = "허브 목록 조회", description = "허브의 전체 목록을 조회합니다. 페이지 크기: 10, 생성순 기본")
    ResponseEntity<ApiResponse<PageResponse<HubListRes>>> getHubList(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);

    @Operation(summary = "허브 상세 조회", description = "허브 ID를 활용해 특정 허브를 상세 조회합니다.")
    @Parameter(name = "hubId", description = "조회할 허브 ID", required = true)
    ResponseEntity<ApiResponse<HubDetailRes>> getHubDetail(@PathVariable UUID hubId);

    @Operation(summary = "허브 수정", description = "허브 ID를 활용해 특정 허브를 수정합니다.")
    @Parameter(name = "hubId", description = "수정할 허브 ID", required = true)
    ResponseEntity<ApiResponse<Void>> updateHub(@PathVariable UUID hubId, @RequestBody UpdateHubReq req);

    @Operation(summary = "허브 삭제", description = "허브 ID를 활용해 특정 허브를 삭제합니다. 허브 삭제 시 관리하는 재고와 관련 이동 경로가 함께 삭제됩니다.")
    @Parameter(name = "hubId", description = "삭제할 허브 ID", required = true)
    ResponseEntity<ApiResponse<Void>> deleteHub(@PathVariable UUID hubId);

    @Operation(summary = "허브 존재 여부 확인", description = "허브 ID를 활용해 특정 허브가 존재하는지 여부를 반환합니다.")
    @Parameter(name = "hubId", description = "확인할 허브 ID", required = true)
    boolean existHub(@PathVariable UUID hubId);

    @Operation(summary = "허브 관리자 ID 반환", description = "허브 ID를 활용해 특정 허브의 관리자 ID를 확인합니다.")
    @Parameter(name = "hubId", description = "허브 ID", required = true)
    ResponseEntity<ApiResponse<HubManagerRes>> getHubManager(@PathVariable UUID hubId);

    @Operation(summary = "허브 재고 생성", description = "허브 ID를 활용해 특정 허브가 관리하는 재고를 생성합니다. 이미 존재하는 상품이라면 재고 개수를 증가시킵니다.")
    @Parameter(name = "hubId", description = "허브 ID", required = true)
    ResponseEntity<ApiResponse<CreateStockRes>> createStock(@PathVariable UUID hubId, @RequestBody CreateStockReq req);

    @Operation(summary = "허브 재고 목록 조회", description = "허브 ID를 활용해 특정 허브가 관리하는 재고 목록을 조회합니다.")
    @Parameter(name = "hubId", description = "허브 ID", required = true)
    ResponseEntity<ApiResponse<List<StockRes>>> getStockList(@PathVariable UUID hubId);

    @Operation(summary = "허브 재고 감소", description = "허브 ID를 활용해 특정 허브가 관리하는 재고의 수량을 감소시킵니다.")
    @Parameter(name = "hubId", description = "허브 ID", required = true)
    ResponseEntity<ApiResponse<Void>> decreaseStock(@PathVariable UUID hubId, @RequestBody UpdateStockReq req);

    @Operation(summary = "허브 재고 증가", description = "허브 ID를 활용해 특정 허브가 관리하는 재고의 수량을 증가시킵니다.")
    @Parameter(name = "hubId", description = "허브 ID", required = true)
    ResponseEntity<ApiResponse<Void>> increaseStock(@PathVariable UUID hubId, @RequestBody UpdateStockReq req);

    @Operation(summary = "허브 재고 조회", description = "허브 ID와 상품 ID를 활용해 특정 상품의 재고 정보를 조회합니다.")
    @Parameters({
            @Parameter(name = "hubId", description = "허브 ID", required = true),
            @Parameter(name = "productId", description = "상품 ID", required = true)
    })
    ResponseEntity<ApiResponse<StockRes>> getStock(@PathVariable UUID hubId, @PathVariable UUID productId);

    @Operation(summary = "허브 재고 삭제", description = "허브 ID와 상품 ID를 활용해 특정 상품의 재고를 삭제합니다.")
    @Parameters({
            @Parameter(name = "hubId", description = "허브 ID", required = true),
            @Parameter(name = "productId", description = "상품 ID", required = true)
    })
    ResponseEntity<ApiResponse<Void>> deleteStock(@PathVariable UUID hubId, @PathVariable UUID stockId);
}
