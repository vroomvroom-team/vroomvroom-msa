package com.vroomvroom.orderservice.domain.repository;

import com.vroomvroom.orderservice.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    /**
     * 주문 저장
     *
     * @param order 저장할 주문
     * @return 저장된 주문
     */
    Order save(Order order);

    /**
     * ID로 주문 조회
     *
     * @param id 주문 ID
     * @return 주문 (없으면 Optional.empty())
     */
    Optional<Order> findById(UUID id);

    /**
     * ID로 주문 조회
     *
     * @param id 주문 ID
     * @return 주문 (삭제되지 않은 주문만, 없으면 Optional.empty())
     */
    Optional<Order> findByIdAndDeletedAtIsNull(UUID id);

    /**
     * 삭제되지 않은 주문 목록 조회 (페이징)
     *
     * @param pageable 페이징 정보
     * @return 주문 페이지
     */
    Page<Order> findAllByDeletedAtIsNull(Pageable pageable);

    /**
     * 주문 목록 전체 조회
     *
     * @return 모든 주문 목록
     */
    List<Order> findAll();

    /**
     * 주문 존재 여부 확인
     *
     * @param id 주문 ID
     * @return 존재 여부
     */
    boolean existsById(UUID id);

    /**
     * ID로 주문 조회
     *
     * @param order 삭제할 주문
     */
    void delete(Order order);
}
