package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRouteRepositoryImpl implements HubRouteRepository {

    private final JpaHubRouteRepository jpaHubRouteRepository;

    @Override
    public Page<HubRoute> findAllByDeletedAtIsNull(Pageable pageable) {
        return jpaHubRouteRepository.findAllByDeletedAtIsNull(pageable);
    }

    @Override
    public Optional<HubRoute> findHubRouteByRouteId(UUID routeId) {
        return jpaHubRouteRepository.findHubRouteByRouteId(routeId);
    }
}
