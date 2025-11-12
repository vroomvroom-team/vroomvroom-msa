package com.vroomvroom.orderservice.presentation.api;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.orderservice.presentation.dto.request.CreateOrderReq;
import com.vroomvroom.orderservice.presentation.dto.request.UpdateOrderReq;
import com.vroomvroom.orderservice.presentation.dto.response.OrderRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "주문 관리", description = "주문 생성, 조회, 수정, 취소 API")
public interface OrderControllerDocs {

    @Operation(
            summary = "주문 생성",
            description = "새로운 주문을 생성합니다. 공급업체, 수령업체, 상품 정보가 필요합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "주문 생성 성공",
                    content = @Content(schema = @Schema(implementation = OrderRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "업체 또는 상품을 찾을 수 없음"
            )
    })
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<OrderRes>> createOrder(@Valid @RequestBody CreateOrderReq request);


    @Operation(
            summary = "주문 단건 조회",
            description = "주문 ID로 특정 주문의 상세 정보를 조회합니다."
    )
    @Parameter(
            name = "orderId",
            description = "조회할 주문의 ID",
            required = true
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<OrderRes>> getOrder(@PathVariable UUID orderId);


    @Operation(
            summary = "주문 전체 목록 조회",
            description = "등록된 주문 전체 목록을 페이징하여 조회합니다."
    )
    @Parameters({
            @Parameter(
                    name = "page",
                    description = "페이지 번호 (0부터 시작)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", defaultValue = "0")
            ),
            @Parameter(
                    name = "size",
                    description = "페이지 크기",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", defaultValue = "10")
            )
    })
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<PageResponse<OrderRes>>> getOrders(Pageable pageable);


    @Operation(
            summary = "주문 취소",
            description = "주문을 취소 처리합니다."
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<OrderRes>> cancelOrder(@PathVariable UUID orderId);


    @Operation(
            summary = "주문 수정",
            description = "기존 주문 정보를 수정합니다."
    )
    ResponseEntity<com.vroomvroom.common.api.ApiResponse<OrderRes>> updateOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody UpdateOrderReq request
    );
}
