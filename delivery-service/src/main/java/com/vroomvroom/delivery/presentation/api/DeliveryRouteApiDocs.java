package com.vroomvroom.delivery.presentation.api;

import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRouteRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "배송경로", description = "배송경로 관련 API")
public interface DeliveryRouteApiDocs {

    @Operation(
        summary = "허브배송 시작",
        description = "허브배송을 시작합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 배송담당자"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "이미 배송중인 담당자"
        )
    })
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<Void>> startHub(
        @PathVariable UUID deliveryId,
        @PathVariable UUID routeId
    );

    @Operation(
        summary = "허브배송 도착",
        description = "허브배송을 완료합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 배송담당자"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "이미 배송중인 담당자"
        )
    })
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<Void>> arriveHub(
        @PathVariable UUID deliveryId,
        @PathVariable UUID routeId
    );

    @Operation(
        summary = "배송에 대한 모든 경로 조회",
        description = "특정 배송에 대한 모든 경로를 조회합니다.")
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 배송"
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<List<GetDeliveryRouteRes>>> getDeliveryAllRoute(
        @PathVariable UUID deliveryId
    );
}
