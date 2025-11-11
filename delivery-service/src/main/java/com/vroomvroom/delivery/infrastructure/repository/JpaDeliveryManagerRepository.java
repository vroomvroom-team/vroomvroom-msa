package com.vroomvroom.delivery.infrastructure.repository;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaDeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {

    boolean existsById(Long userId);

    Optional<DeliveryManager> findById(Long userId);

    // 타입별 배송 담당자 조회
    Page<DeliveryManager> findAllByTypeAndDeletedAtIsNull(DeliveryManagerType type,
        Pageable pageable);

    Page<DeliveryManager> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<DeliveryManager> findByIdAndDeletedAtIsNull(Long id);

    // 허브 매니저(전역)
    @Query(value = """
               SELECT s.seq
               FROM generate_series(0, 9) AS s(seq)
               LEFT JOIN p_delivery_manager dm
                      ON dm.sequence = s.seq
                     AND dm.type = 'HUB_MANAGER'
                     AND dm.deleted_at IS NULL
               WHERE dm.sequence IS NULL
               ORDER BY s.seq
               LIMIT 1
        """, nativeQuery = true)
    Long nextGlobalSequence();


    // 업체 매니저(허브별)
    @Query(value = """
               SELECT s.seq
               FROM generate_series(0, 9) AS s(seq)
               LEFT JOIN p_delivery_manager dm
                      ON dm.sequence = s.seq
                     AND dm.type = 'COMPANY_MANAGER'
                     AND dm.hub_id = :hubId
                     AND dm.deleted_at IS NULL
               WHERE dm.sequence IS NULL
               ORDER BY s.seq
               LIMIT 1
        """, nativeQuery = true)
    Long nextHubSequence(@Param("hubId") UUID hubId);


    @Query(value = """
                SELECT dm
                FROM DeliveryManager dm
                WHERE dm.sequence.value = :sequence
                  AND dm.type = :type
                  AND dm.isActive = :isActive
        """)
    Optional<DeliveryManager> findBySequenceAndTypeAndIsActiveFalse(
        @Param("sequence") Long sequence,
        @Param("type") DeliveryManagerType type,
        @Param("isActive") Boolean isActive
    );
}
