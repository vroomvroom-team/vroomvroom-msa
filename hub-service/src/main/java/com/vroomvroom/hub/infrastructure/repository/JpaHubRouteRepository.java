package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaHubRouteRepository extends JpaRepository<HubRoute, UUID> {
    @Query("SELECT h FROM HubRoute h " +
            "JOIN FETCH h.departureHub " +
            "JOIN FETCH h.arrivalHub " +
            "WHERE h.deletedAt IS NULL")
    Page<HubRoute> findAllWithHubs(Pageable pageable);

    @Query("SELECT h FROM HubRoute h " +
            "JOIN FETCH h.departureHub " +
            "JOIN FETCH h.arrivalHub " +
            "WHERE h.routeId = :routeId " +
            "AND h.deletedAt IS NULL")
    Optional<HubRoute> findHubRouteWithHubsByRouteId(@Param("routeId") UUID routeId);

    @Query("SELECT h FROM HubRoute h " +
            "WHERE h.deletedAt IS NULL " +
            "AND h.isActive = true ")
    List<HubRoute> findAllActive();

    @Query("SELECT h FROM HubRoute h " +
            "WHERE (h.departureHub.hubId = :hubId OR h.arrivalHub.hubId = :hubId) " +
            "AND h.deletedAt IS NULL")
    List<HubRoute> findAllByHubId(@Param("hubId") UUID hubId);
}