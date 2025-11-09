package com.vroomvroom.hub.domain.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository {
    HubRoute save(HubRoute hubRoute);
    Page<HubRoute> findAllWithHubs(Pageable pageable);
    Optional<HubRoute> findHubRouteWithHubsByRouteId(UUID routeId);
    boolean existsByDepartureHub_HubIdAndArrivalHub_HubId(UUID departureHubId, UUID arrivalHubId);
}
