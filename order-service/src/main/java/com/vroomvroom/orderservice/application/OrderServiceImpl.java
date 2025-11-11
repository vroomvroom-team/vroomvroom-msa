package com.vroomvroom.orderservice.application;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.orderservice.application.command.CancelOrderCommand;
import com.vroomvroom.orderservice.application.command.CreateOrderCommand;
import com.vroomvroom.orderservice.application.command.UpdateOrderCommand;
import com.vroomvroom.orderservice.application.dto.OrderDTO;
import com.vroomvroom.orderservice.application.service.CompanyClient;
import com.vroomvroom.orderservice.application.service.ProductClient;
import com.vroomvroom.orderservice.domain.entity.Order;
import com.vroomvroom.orderservice.domain.repository.OrderRepository;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.domain.vo.OrderStatus;
import com.vroomvroom.orderservice.exception.OrderErrorCode;
import com.vroomvroom.orderservice.infrastructure.dto.CompanyHubDTO;
import com.vroomvroom.orderservice.infrastructure.dto.ProductDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final CompanyClient companyClient;

    /**
     * 주문 생성
     *
     * @param command 주문 생성 요청 Command
     * @return 주문 응답 DTO
     */
    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderCommand command) {
        log.info("주문 생성 시작 - 요청 업체 ID={}, 공급 업체 ID={}, 상품 ID={}",
                command.receiveCompanyId(), command.supplyCompanyId(), command.productId());

        // 외부 정보 조회
        CompanyHubDTO supplyCompany = companyClient.getCompanyHubInfo(command.supplyCompanyId());
        CompanyHubDTO receiveCompany = companyClient.getCompanyHubInfo(command.receiveCompanyId());
        ProductDTO product = productClient.getProductInfo(command.productId());

        // 주문 생성
        Order order = Order.create(
                supplyCompany.getCompanyId(),
                receiveCompany.getCompanyId(),
                supplyCompany.getHubId(),
                receiveCompany.getHubId(),
                product.getProductId(),
                product.getPrice(),
                command.quantity(),
                command.deadline(),
                command.requestNote()
        );

        // 재고 차감
        decreaseStocks(order.getProductId(), order.getQuantity());

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // TODO. 주문 저장 실패 시 재고 원복 로직 필요

        log.info("주문 생성 성공 - 주문 ID={}", savedOrder.getId());
        return OrderDTO.from(savedOrder);
    }

    // 상품 재고 감소
    private void decreaseStocks(UUID productId, Long quantity) {
        // TODO. 상품 재고 감소 로직
        try {
            if (!productClient.decreaseStocks(productId, quantity))
                throw new CustomException(OrderErrorCode.PRODUCT_STOCK_DECREASE_FAILED);

        } catch (FeignException e) {
            throw new CustomException(OrderErrorCode.PRODUCT_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 주문 단건 조회
     * 삭제된 주문은 제외
     *
     * @param orderId 주문 ID
     * @return 주문 응답 DTO
     */
    @Override
    public OrderDTO getOrder(UUID orderId) {
        log.info("주문 조회 - 주문 ID={}", orderId);

        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        return OrderDTO.from(order);
    }

    /**
     * 주문 목록 조회 (페이징)
     * 삭제된 주문은 제외
     *
     * @param pageable 페이징 정보
     * @return 주문 목록
     */
    @Override
    public Page<OrderDTO> getOrders(Pageable pageable) {
        log.info("주문 조회 페이징 - pagination={}", pageable);

        return orderRepository.findAllByDeletedAtIsNull(pageable)
                .map(OrderDTO::from);
    }

    /**
     * 주문 취소
     * 배송 전 주문만 취소 가능
     *
     * @param command 유저 ID, 및 주문 ID Command
     */
    @Transactional
    @Override
    public OrderDTO cancelOrder(CancelOrderCommand command) {
        log.info("주문 취소 시작 - 유저 ID={}, 주문 ID={}",
                command.userId(), command.orderId());

        Order order = orderRepository.findByIdAndDeletedAtIsNull(command.orderId())
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        // 배송 전 주문만 취소 가능
        if (!order.isCancellable()) {
            log.debug("주문 취소 불가 상태 - 주문 상태={}", order.getOrderStatus());
            throw new CustomException(OrderErrorCode.ORDER_NOT_CANCELLABLE);
        }

        order.updateStatus(OrderStatus.CANCELLED);
        order.markAsDeleted();
        log.info("주문 취소 성공 - 주문 ID={}", order.getId());

        return OrderDTO.from(order);
    }

    /**
     * 주문 수정
     * 배송 전 주문만 수정 가능
     *
     */
    @Transactional
    @Override
    public OrderDTO updateOrder(UpdateOrderCommand command) {
        log.info("주문 수정 시작 - 주문 ID={}", command.orderId());

        // TODO. 유저 ID 유효성 검증 필요

        Order order = orderRepository.findByIdAndDeletedAtIsNull(command.orderId())
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        if (!order.isModifiable()) {
            log.debug("주문 수정 불가 상태 - 주문 상태={}", order.getOrderStatus());
            throw new CustomException(OrderErrorCode.ORDER_NOT_MODIFIABLE);
        }

        Money newTotalPrice = order.getTotalPrice();
        Long quantityDiff = null;

        // 수량 변경 시 재고 및 금액 재계산
        if (command.quantity() != null && !command.quantity().equals(order.getQuantity())) {
            // 상품 정보 조회
            ProductDTO product = productClient.getProductInfo(order.getProductId());

            // 수량 차이 계산
            quantityDiff = command.quantity() - order.getQuantity();

            // 재고 확인
            if (quantityDiff > 0) {
                decreaseStocks(order.getProductId(), quantityDiff);
            } else if (quantityDiff < 0)
                productClient.increaseStocks(order.getProductId(), Math.abs(quantityDiff));

            // 총 금액 재계산
            newTotalPrice = product.getPrice().multiply(command.quantity());
        }

        // 주문 수정
        try {
            order.update(
                    command.quantity(),
                    command.deadline(),
                    command.requestNote(),
                    newTotalPrice
            );
        } catch (Exception e) {
            // 재고 변동 시 원래 주문 수량으로 원복
            if (quantityDiff != null)
                rollbackStock(order.getProductId(), quantityDiff);
        }
        log.info("주문 수정 완료 - 주문 ID={}", command.orderId());
        return OrderDTO.from(order);
    }

    private void rollbackStock(UUID productId, long quantityDiff) {
        try {
            if (quantityDiff > 0) {
                productClient.increaseStocks(productId, quantityDiff);
            } else if (quantityDiff < 0)
                productClient.decreaseStocks(productId, Math.abs(quantityDiff));

        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.PRODUCT_INTERNAL_SERVER_ERROR);
        }
    }
}
