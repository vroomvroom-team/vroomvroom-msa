package com.vroomvroom.orderservice.application;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.common.exception.ErrorCode;
import com.vroomvroom.orderservice.application.command.CancelOrderCommand;
import com.vroomvroom.orderservice.application.command.CreateOrderCommand;
import com.vroomvroom.orderservice.application.dto.OrderRes;
import com.vroomvroom.orderservice.application.service.CompanyClient;
import com.vroomvroom.orderservice.application.service.ProductClient;
import com.vroomvroom.orderservice.domain.entity.Order;
import com.vroomvroom.orderservice.domain.repository.OrderRepository;
import com.vroomvroom.orderservice.domain.vo.Money;
import com.vroomvroom.orderservice.domain.vo.OrderStatus;
import com.vroomvroom.orderservice.infrastructure.dto.CompanyHubDTO;
import com.vroomvroom.orderservice.infrastructure.dto.ProductDTO;
import feign.FeignException;
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
    public OrderRes createOrder(CreateOrderCommand command) {
        log.info("주문 생성 시작 - 요청 업체 ID={}, 공급 업체 ID={}, 상품 ID={}",
                command.receiveCompanyId(), command.supplyCompanyId(), command.productId());

        // 외부 업체 서비스 호출
        CompanyHubDTO supplyCompany = getCompanyHubInfo(command.supplyCompanyId());
        CompanyHubDTO receiveCompany = getCompanyHubInfo(command.receiveCompanyId());

        // 외부 상품 서비스 호출 및 총 금액 계산
        ProductDTO product = getProductInfo(command.productId(), command.quantity());
        Money totalPrice = product.getPrice().multiply(command.quantity());

        // 주문 생성
        Order order = Order.builder()
                .supplyCompanyId(supplyCompany.getCompanyId())
                .receiveCompanyId(receiveCompany.getCompanyId())
                .supplyHubId(supplyCompany.getHubId())
                .receiveHubId(receiveCompany.getHubId())
                .productId(product.getProductId())
                .totalPrice(totalPrice)
                .quantity(command.quantity())
                .deadline(command.deadline())
                .requestNote(command.requestNote())
                .orderStatus(OrderStatus.PENDING)
                .build();

        // 재고 차감
        decreaseStocks(order.getProductId(), order.getQuantity());

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // TODO. 주문 저장 실패 시 재고 원복 로직 필요

        log.info("주문 생성 성공 - 주문 ID={}", savedOrder.getId());
        return OrderRes.from(order);
    }

    // 업체별 소속 허브 정보 가져오기
    private CompanyHubDTO getCompanyHubInfo(UUID companyId) {
        // TODO. 업체 서비스 API 호출
        try {
            return companyClient.getCompanyHubInfo(companyId);
        } catch (FeignException e) {
            // e "업체 정보 조회 실패"
            throw new RuntimeException(e);
        }
    }

    // 상품 정보 가져오기
    private ProductDTO getProductInfo(UUID productId, BigInteger quantity) {
        // TODO. 상품 서비스 API 호출
        try {
            return productClient.getProductInfo(productId);
        } catch (FeignException e) {
            // e "상품 정보 조회 실패"
            throw new RuntimeException(e);
        }
    }

    // 상품 재고 감소
    private void decreaseStocks(UUID productId, BigInteger quantity) {
        // TODO. 상품 재고 감소 로직
        try {
            if (!productClient.decreaseStocks(productId, quantity))
                throw new RuntimeException("상품 재고 감소 실패");

        } catch (FeignException e) {
            // e "상품 정보 조회 실패"
            throw new RuntimeException(e);
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
    public OrderRes getOrder(UUID orderId) {
        log.info("주문 조회 - 주문 ID={}", orderId);

        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)); // TODO. ErrorCode 관리

        return OrderRes.from(order);
    }

    /**
     * 주문 목록 조회 (페이징)
     * 삭제된 주문은 제외
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

    /**
     * 주문 취소
     * 배송 전 주문만 취소 가능
     *
     * @param command 유저 ID, 및 주문 ID Command
     */
    @Transactional
    @Override
    public void cancelOrder(CancelOrderCommand command) {
        // 배송
    }

    /**
     * 주문 수정
     * 배송 전 주문만 수정 가능
     *
     * @return 주문 응답 DTO
     */
    @Transactional
    @Override
    public OrderRes updateOrder() {
        return null;
    }

}
