package com.vroomvroom.hub.domain.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository {
    Page<HubRoute> findAllByDeletedAtIsNull(Pageable pageable);
    Optional<HubRoute> findHubRouteByRouteId(UUID routeId);
}
