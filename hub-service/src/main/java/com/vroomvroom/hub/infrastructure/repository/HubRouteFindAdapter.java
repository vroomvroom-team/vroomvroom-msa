package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.repository.HubRouteFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubRouteFindAdapter implements HubRouteFindRepository {

    private final JpaHubRouteRepository jpaHubRouteRepository;

    @Override
    public Page<HubRoute> findAllWithHubs(Pageable pageable) {
        return jpaHubRouteRepository.findAllWithHubs(pageable);
    }

    @Override
    public Optional<HubRoute> findHubRouteWithHubsByRouteId(UUID routeId) {
        return jpaHubRouteRepository.findHubRouteWithHubsByRouteId(routeId);
    }

    @Override
    public List<HubRoute> findAllActive() {
        return jpaHubRouteRepository.findAllActive();
    }
}