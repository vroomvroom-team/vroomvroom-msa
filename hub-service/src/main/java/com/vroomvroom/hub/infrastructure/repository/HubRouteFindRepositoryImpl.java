package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.repository.HubRouteFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRouteFindRepositoryImpl implements HubRouteFindRepository {

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

    @Override
    public List<HubRoute> findAllByHubId(UUID hubId) {
        return jpaHubRouteRepository.findAllByHubId(hubId);
    }
}