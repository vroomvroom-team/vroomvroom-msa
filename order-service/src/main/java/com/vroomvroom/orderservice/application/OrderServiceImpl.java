package com.vroomvroom.orderservice.application;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.orderservice.application.command.CancelOrderCommand;
import com.vroomvroom.orderservice.application.command.CreateOrderCommand;
import com.vroomvroom.orderservice.application.command.UpdateOrderCommand;
import com.vroomvroom.orderservice.application.dto.OrderDTO;
import com.vroomvroom.orderservice.application.service.CompanyClient;
import com.vroomvroom.orderservice.application.service.HubClient;
import com.vroomvroom.orderservice.application.service.ProductClient;
import com.vroomvroom.orderservice.domain.entity.Order;
import com.vroomvroom.orderservice.domain.event.OrderCreatedEvent;
import com.vroomvroom.orderservice.domain.port.OrderEventPublisher;
import com.vroomvroom.orderservice.domain.repository.OrderRepository;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.domain.vo.OrderStatus;
import com.vroomvroom.orderservice.exception.OrderErrorCode;
import com.vroomvroom.orderservice.infrastructure.dto.CompanyHubDTO;
import com.vroomvroom.orderservice.infrastructure.dto.ProductDTO;
import com.vroomvroom.orderservice.infrastructure.dto.StockDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final CompanyClient companyClient;
    private final HubClient hubClient;
    private final OrderEventPublisher orderEventPublisher;

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
        StockDTO stock = hubClient.getStockInfo(supplyCompany.getHubId(), product.getProductId());

        // 공급/수량 업체 동일 여부 검증
        if (supplyCompany.getCompanyId().equals(receiveCompany.getCompanyId()))
            throw new CustomException(OrderErrorCode.SAME_SUPPLY_RECEIVE_COMPANY);
        // 재고 수량 확인
        if (stock.getQuantity() <= 0)
            throw new CustomException(OrderErrorCode.HUB_PRODUCT_OUT_OF_STOCK);

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
        hubClient.decreaseStocks(order.getProductId(), order.getQuantity());

        try {
            // 주문 저장
            Order savedOrder = orderRepository.save(order);

            // 트랜잭션 커밋 후 이벤트 발행
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishOrderCreatedEvent(savedOrder);
                }
            });

            log.info("주문 생성 성공 - 주문 ID={}", savedOrder.getId());
            return OrderDTO.from(savedOrder);

        } catch (Exception e) {
            // 주문 저장 실패 시 감소된 재고 원복
            hubClient.increaseStocks(order.getProductId(), order.getQuantity());
        }

        return null;
    }

    // 주문 생성 이벤트 발행
    private void publishOrderCreatedEvent(Order order) {
        try {
            OrderCreatedEvent event = OrderCreatedEvent.from(
                    order.getId(),
                    order.getSupplyHubId(),
                    order.getReceiveHubId()
            );

            orderEventPublisher.publishOrderCreated(order.getId(), event);
            log.info("주문 생성 이벤트 발행 완료 - orderId: {}", order.getId());

        } catch (Exception e) {
            log.error("주문 생성 이벤트 발행 실패 - orderId: {}", order.getId(), e);
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
                hubClient.decreaseStocks(order.getProductId(), quantityDiff);
            } else if (quantityDiff < 0)
                hubClient.increaseStocks(order.getProductId(), Math.abs(quantityDiff));

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
            // 주문 수정 오류 시 원래 주문 수량으로 원복
            if (quantityDiff != null)
                rollbackStock(order.getProductId(), quantityDiff);
        }
        log.info("주문 수정 완료 - 주문 ID={}", command.orderId());
        return OrderDTO.from(order);
    }

    // 재고 원복 로직
    private void rollbackStock(UUID productId, long quantityDiff) {
        try {
            if (quantityDiff > 0) {
                hubClient.increaseStocks(productId, quantityDiff);
            } else if (quantityDiff < 0)
                hubClient.decreaseStocks(productId, Math.abs(quantityDiff));

        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.PRODUCT_INTERNAL_SERVER_ERROR);
        }
    }
}
