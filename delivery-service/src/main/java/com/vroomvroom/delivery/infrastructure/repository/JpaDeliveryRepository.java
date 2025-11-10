package com.vroomvroom.delivery.infrastructure.repository;

import com.vroomvroom.delivery.domain.entity.Delivery;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID> {

    @Query(value = """
            SELECT dr
            FROM DeliveryRoute dr
            WHERE dr.id = :routeId
        """)
    Optional<DeliveryRoute> findByRouteId(@Param("routeId") UUID routeId);


    @Query(value = """
            SELECT dr
            FROM DeliveryRoute dr
            WHERE dr.delivery.id = :deliveryId
              AND dr.sequence.value = :nextSequence
        """)
    Optional<DeliveryRoute> findByDeliveryIdAndSequence(
        @Param("deliveryId") UUID deliveryId,
        @Param("nextSequence") Long nextSequence
    );


    @Query("""
          SELECT dr.id as routeId, d.id as deliveryId
          FROM Delivery d
          JOIN d.deliveryRoutes dr
          WHERE dr.deliveryManagerId is null
            AND dr.status = com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus.HUB_MOVE_WAITING
          ORDER BY d.createdAt ASC, dr.sequence.value ASC
        """)
    Page<UnassignedRouteIds> findUnassignedRouteKeys(Pageable pageable);
}
