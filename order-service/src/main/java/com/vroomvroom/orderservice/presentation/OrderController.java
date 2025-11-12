package com.vroomvroom.orderservice.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.orderservice.application.OrderService;
import com.vroomvroom.orderservice.application.command.CancelOrderCommand;
import com.vroomvroom.orderservice.application.command.CreateOrderCommand;
import com.vroomvroom.orderservice.application.command.UpdateOrderCommand;
import com.vroomvroom.orderservice.application.dto.OrderDTO;
import com.vroomvroom.orderservice.presentation.api.OrderControllerDocs;
import com.vroomvroom.orderservice.presentation.dto.request.CreateOrderReq;
import com.vroomvroom.orderservice.presentation.dto.request.UpdateOrderReq;
import com.vroomvroom.orderservice.presentation.dto.response.OrderRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController implements OrderControllerDocs {

    private final OrderService orderService;

    /**
     * 주문 생성
     * <p>
     * POST /api/v1/orders
     *
     * @param request 주문 생성 요청
     * @return 생성된 주문 정보
     */
    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<OrderRes>> createOrder(
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

        // TODO. 배송 정보 반영 필요

        OrderDTO orderDTO = orderService.createOrder(command);

        OrderRes response = OrderRes.from(orderDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("주문이 생성되었습니다.", response));
    }

    /**
     * 주문 단건 조회
     * <p>
     * GET /api/v1/orders/{orderId}
     *
     * @param orderId 주문 ID
     * @return 주문 정보
     */
    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderRes>> getOrder(@PathVariable UUID orderId) {
        log.info("GET /api/v1/orders/{} - 주문 조회", orderId);

        OrderDTO orderDTO = orderService.getOrder(orderId);

        OrderRes response = OrderRes.from(orderDTO);

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
    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderRes>>> getOrders(
            @PageableDefault(size = 10, page = 0)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        log.info("GET /api/v1/orders - 주문 전체 목록 조회");

        Page<OrderDTO> orderDTOPage = orderService.getOrders(pageable);

        Page<OrderRes> orderResPage = orderDTOPage.map(OrderRes::from);

        PageResponse<OrderRes> response = PageResponse.fromPage(orderResPage);

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
    @Override
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderRes>> cancelOrder(@PathVariable UUID orderId) {
        log.info("DELETE /api/v1/orders/{} - 주문 취소", orderId);

        // TODO. 유저 ID 반영 필요
        CancelOrderCommand command = new CancelOrderCommand(UUID.randomUUID(), orderId);

        OrderDTO orderDTO = orderService.cancelOrder(command);

        OrderRes response = OrderRes.from(orderDTO);

        return ResponseEntity.ok(
                ApiResponse.success("주문이 취소되었습니다.", response));
    }

    /**
     * 주문 수정
     * <p>
     * PATCH /api/v1/orders/{orderId}
     *
     * @param orderId 주문 ID
     * @return OK
     */
    @Override
    @PatchMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderRes>> updateOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody UpdateOrderReq request) {
        log.info("PATCH /api/v1/orders/{} - 주문 수정", orderId);

        // TODO. 유저 ID 반영 필요
        UpdateOrderCommand command = new UpdateOrderCommand(
                UUID.randomUUID(),
                orderId,
                request.getQuantity(),
                request.getDeadline(),
                request.getRequestNote());

        OrderDTO orderDTO = orderService.updateOrder(command);

        OrderRes response = OrderRes.from(orderDTO);

        return ResponseEntity.ok(
                ApiResponse.success("주문이 수정되었습니다.", response));
    }
}
