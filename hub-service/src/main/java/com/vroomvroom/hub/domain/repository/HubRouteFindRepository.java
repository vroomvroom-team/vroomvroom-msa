package com.vroomvroom.hub.domain.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRouteFindRepository {
    Page<HubRoute> findAllWithHubs(Pageable pageable);
    Optional<HubRoute> findHubRouteWithHubsByRouteId(UUID routeId);
    List<HubRoute> findAllActive();
    List<HubRoute> findAllByHubId(@Param("hubId") UUID hubId);
}