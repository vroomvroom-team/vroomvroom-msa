package com.vroomvroom.delivery.domain.repository;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository {

    DeliveryManager save(DeliveryManager deliveryManager);

    boolean existsByDeliveryManagerId(Long userId);

    Page<DeliveryManager> findAllDelivery(Pageable pageable);

    Page<DeliveryManager> findAllByType(DeliveryManagerType type, Pageable pageable);

    Optional<DeliveryManager> findDeliveryById(Long id);

    Long nextGlobalSequence();

    Long nextHubSequence(UUID hubId);

    /**
     * 지정된 시퀀스와 타입을 가진 배송담당자 중 일하고 있지 않은(isActive = false)인 담당자 조회
     *     - 현재 일할 수 있는(대기 중) 담당자 찾는 용도
     */
    Optional<DeliveryManager> findBySequenceAndTypeAndIsActiveFalse(
        Long sequenceToAssign, DeliveryManagerType deliveryManagerType, boolean isActive);
}
