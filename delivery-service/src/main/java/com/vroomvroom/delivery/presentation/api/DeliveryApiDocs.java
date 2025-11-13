package com.vroomvroom.delivery.presentation.api;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.delivery.presentation.dto.request.CreateDeliveryReq;
import com.vroomvroom.delivery.presentation.dto.response.CreateDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.GetAllDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "배송", description = "배송 관련 API")
public interface DeliveryApiDocs {

    @Operation(
        summary = "배송 생성",
        description = "배송과 배송경로를 생성하고, 첫번째 경로에 담당자를 배정합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "배송 생성 성공"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "배송 ID를 찾을 수 없음"
        )
    })
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<CreateDeliveryRes>> createDelivery(
        @Valid @RequestBody CreateDeliveryReq request
    );

    @Operation(
        summary = "마지막 허브에서 업체 배송으로 전환",
        description = "허브 간 이동이 완료된 배송에 대해 업체로의 배소을 시작하고, COMPANY_MANAGER를 배정합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "허브에서 업체 배송을 전환 성공"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "배송 ID를 찾을 수 없음"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "잘못된 배송 상태"
        )
    })
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<Void>> handoffToCompany(
        @PathVariable UUID deliveryId
    );

    @Operation(
        summary = "배송 완료",
        description = "배송을 완료합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "허브에서 업체 배송을 전환 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "배송담당자 ID를 찾을 수 없음"
        )
    })
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<Void>> completeDelivery(
        @Valid @PathVariable UUID deliveryId,
        @Valid @RequestHeader("X-User-Id") Long userId,
        @Valid @RequestHeader("X-User-Role") String userRole
    );

    @Operation(
        summary = "모든 배송 조회",
        description = "모든 배송을 조회합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "모든 배송 조회 성공"
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<PageResponse<GetAllDeliveryRes>>>
    getAllDelivery(
        @SortDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable
    );

    @Operation(
        summary = "배송 상세조회",
        description = "배송에 대한 정보를 상세 조회합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "배송 상세 조회 성공"
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<GetDeliveryRes>> getDelivery(
        @PathVariable UUID deliveryId
    );

    @Operation(
        summary = "배송 취소",
        description = "배송을 취소합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "취소 성공"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 배송"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "배송 취소가 불가능한 상태"
        )
    })
    public ResponseEntity<com.vroomvroom.common.api.ApiResponse<Void>> cancelDelivery(
        @PathVariable UUID deliveryId
    );


}
