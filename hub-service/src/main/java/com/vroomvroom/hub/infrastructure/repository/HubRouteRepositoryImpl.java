package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRouteRepositoryImpl implements HubRouteRepository {

    private final JpaHubRouteRepository jpaHubRouteRepository;

    @Override
    public HubRoute save(HubRoute hubRoute) {
        return jpaHubRouteRepository.save(hubRoute);
    }

    @Override
    public Page<HubRoute> findAllWithHubs(Pageable pageable) {
        return jpaHubRouteRepository.findAllWithHubs(pageable);
    }

    @Override
    public Optional<HubRoute> findHubRouteWithHubsByRouteId(UUID routeId) {
        return jpaHubRouteRepository.findHubRouteWithHubsByRouteId(routeId);
    }

    @Override
    public boolean existsByDepartureHub_HubIdAndArrivalHub_HubId(UUID departureHubId, UUID arrivalHubId) {
        return jpaHubRouteRepository.existsByDepartureHub_HubIdAndArrivalHub_HubId(departureHubId, arrivalHubId);
    }

    @Override
    public List<HubRoute> findAllActive() {
        return jpaHubRouteRepository.findAllActive();
    }
}
