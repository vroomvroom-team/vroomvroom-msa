package com.vroomvroom.orderservice.application;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.common.exception.ErrorCode;
import com.vroomvroom.orderservice.application.command.CreateOrderCommand;
import com.vroomvroom.orderservice.application.dto.OrderRes;
import com.vroomvroom.orderservice.domain.entity.Order;
import com.vroomvroom.orderservice.domain.repository.OrderRepository;
import com.vroomvroom.orderservice.domain.vo.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    /**
     * 주문 생성
     *
     * @param command 주문 생성 요청 Command
     * @return 주문 응답 DTO
     */
    @Override
    @Transactional
    public OrderRes createOrder(CreateOrderCommand command) {
        log.info("주문 생성 시작 - 요청 업체 ID={}, 공급 업체 ID={}, 상품 ID={}",
                command.receiveCompanyId(), command.supplyCompanyId(), command.productId());

        validateProduct(command.productId(), command.quantity());

        Order order = Order.builder()
                .supplyCompanyId(command.supplyCompanyId())
                .receiveCompanyId(command.receiveCompanyId())
                .supplyHubId(command.supplyHubId())
                .receiveHubId(command.receiveHubId())
                .productId(command.productId())
                .totalPrice(command.totalPrice())
                .quantity(command.quantity())
                .deadline(command.deadline())
                .requestNote(command.requestNote())
                .orderStatus(OrderStatus.PENDING)
                .build();

        decreaseStocks(order.getProductId(), order.getQuantity());
        Order savedOrder = orderRepository.save(order);

        log.info("주문 생성 성공 - 주문 ID={}", savedOrder.getId());
        return OrderRes.from(order);
    }

    // 상품 확인
    private void validateProduct(UUID productId, BigInteger quantity) {
        // TODO.상품 ID 확인

        // TODO. 상품 재고 확인
    }

    // 상품 재고 감소
    private void decreaseStocks(UUID productId, BigInteger quantity) {
        // TODO. 상품 재고 감소 로직
    }

    /**
     * 주문 단건 조회
     *
     * @param orderId 주문 ID
     * @return 주문 응답 DTO
     */
    @Override
    public OrderRes getOrder(UUID orderId) {
        log.info("주문 조회 - 주문 ID={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)); // TODO. ErrorCode 관리

        return OrderRes.from(order);
    }

    /**
     * 주문 목록 조회 (페이징)
     *
     * @param pageable 페이징 정보
     * @return 주문 목록
     */
    @Override
    public Page<OrderRes> getOrders(Pageable pageable) {
        log.info("주문 조회 페이징 - pagination={}", pageable);

        return orderRepository.findAllByDeletedAtIsNull(pageable)
                .map(OrderRes::from);
    }

}
