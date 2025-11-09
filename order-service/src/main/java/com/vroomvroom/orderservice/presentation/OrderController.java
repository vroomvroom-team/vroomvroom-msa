package com.vroomvroom.orderservice.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.orderservice.application.OrderService;
import com.vroomvroom.orderservice.application.command.CancelOrderCommand;
import com.vroomvroom.orderservice.application.command.CreateOrderCommand;
import com.vroomvroom.orderservice.application.dto.OrderRes;
import com.vroomvroom.orderservice.presentation.dto.request.CreateOrderReq;
import com.vroomvroom.orderservice.presentation.dto.response.CreateOrderRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     * <p>
     * POST /api/v1/orders
     *
     * @param request 주문 생성 요청
     * @return 생성된 주문 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderRes>> createOrder(
            @Valid @RequestBody CreateOrderReq request) {
        log.info("POST /api/v1/orders - 주문 생성 요청");

        // TODO. 유저 ID 반영 필요

        CreateOrderCommand command = new CreateOrderCommand(
                request.getSupplyCompanyId(),
                request.getReceiveCompanyId(),
                request.getProductId(),
                request.getQuantity(),
                request.getDeadline(),
                request.getRequestNote()
        );

        UUID orderId = orderService.createOrder(command).getOrderId();

        // TODO. 배송 정보 반영 필요

        CreateOrderRes response = new CreateOrderRes(orderId, "주문이 성공적으로 생성되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    /**
     * 주문 단건 조회
     * <p>
     * GET /api/v1/orders/{orderId}
     *
     * @param orderId 주문 ID
     * @return 주문 정보
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderRes>> getOrder(@PathVariable UUID orderId) {
        log.info("GET /api/v1/orders/{} - 주문 조회", orderId);

        OrderRes response = orderService.getOrder(orderId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 주문 전체 목록 조회
     * <p>
     * GET /api/v1/orders
     *
     * @param pageable 페이징 정보 (page, size, sort)
     * @return 주문 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderRes>>> getOrders(Pageable pageable) {
        log.info("GET /api/v1/orders - 주문 전체 목록 조회");

        Page<OrderRes> page = orderService.getOrders(pageable);

        PageResponse<OrderRes> response = PageResponse.fromPage(page);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 주문 취소
     * <p>
     * DELETE /api/v1/orders/{orderId}
     *
     * @param orderId 주문 ID
     * @return OK
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<String>> cancelOrder(@PathVariable UUID orderId) {
        log.info("DELETE /api/v1/cancel/{} - 주문 취소", orderId);

        // TODO. 유저 ID 반영 필요
        CancelOrderCommand command = new CancelOrderCommand(UUID.randomUUID(), orderId);

        orderService.cancelOrder(command);

        return ResponseEntity.ok(ApiResponse.success("주문 취소 성공"));
    }
}
