package com.vroomvroom.delivery.presentation.api;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.presentation.dto.request.CreateManagerReq;
import com.vroomvroom.delivery.presentation.dto.response.CreateManagerRes;
import com.vroomvroom.delivery.presentation.dto.response.DeliveryManagerRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "배송담당자", description = "배송담당자 관련 API")
public interface DeliveryManagerApiDocs {

    @Operation(
        summary = "배송 담당자 생성",
        description = "배송 담당자를 생성합니다.")
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 배송담당자"
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<CreateManagerRes>> createDeliveryManager(
        @Valid @RequestBody CreateManagerReq request
    );

    @Operation(
        summary = "배송 담당자 전체 조회",
        description = "배송 담당자 전체를 조회합니다.")
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 배송담당자"
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<PageResponse<DeliveryManagerRes>>> getAllDeliveryManager(
        @RequestParam(required = false) DeliveryManagerType type,
        Pageable pageable);

    @Operation(
        summary = "배송 담당자 조회",
        description = "배송 담당자를 조회합니다.")
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 배송담당자"
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<DeliveryManagerRes>> getDeliveryManagerById(
        @PathVariable Long deliveryId);

    @Operation(
        summary = "배송 담당자 삭제",
        description = "배송 담당자 삭제합니다.")
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 배송담당자"
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<DeliveryManagerRes>> deleteDeliveryManagerById(
        @PathVariable Long deliveryId);
}
