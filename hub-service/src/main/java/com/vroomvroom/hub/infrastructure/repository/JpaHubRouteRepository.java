package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaHubRouteRepository extends JpaRepository<HubRoute, UUID> {
    Page<HubRoute> findAllByDeletedAtIsNull(Pageable pageable);
    Optional<HubRoute> findHubRouteByRouteId(UUID routeId);
}
