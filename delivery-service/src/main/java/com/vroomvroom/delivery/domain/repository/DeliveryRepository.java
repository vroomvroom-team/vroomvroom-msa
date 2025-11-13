package com.vroomvroom.delivery.domain.repository;

import com.vroomvroom.delivery.domain.entity.Delivery;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.vo.DeliveryStatus;
import com.vroomvroom.delivery.infrastructure.repository.UnassignedRouteIds;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepository {

    /**
     * 배송 저장
     *
     * @param delivery 저장할 배송
     * @return 저장된 배송
     */
    Delivery save(Delivery delivery);

    Optional<DeliveryRoute> findByRouteId(UUID routeId);

    /**
     * deliveryId와 nextSeq를 가진 Delivery Route
     */
    Optional<DeliveryRoute> findByDeliveryIdAndSequence(
        UUID deliveryId, Long nextSequence);

    /**
     * 담당자가 아직 배정되지 않은 배송대기 상태의 deliveryRouteId, deliveryId 가져옴 - 가장 먼저 생긴 배송 우선
     */
    Page<UnassignedRouteIds> findUnassignedRouteKeys(Pageable pageable);

    Optional<Delivery> findById(UUID deliveryId);

    Page<Delivery> findAllByDeletedAtIsNull(Pageable pageable);

    Page<UUID> findIdsByStatus(DeliveryStatus deliveryStatus, UUID hubId, Pageable pageable);

    List<DeliveryRoute> findAllByDeliveryIdOrderBySequenceAsc(UUID deliveryId);
}
