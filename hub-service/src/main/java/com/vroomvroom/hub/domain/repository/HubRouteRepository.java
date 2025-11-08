package com.vroomvroom.hub.domain.repository;

import com.vroomvroom.hub.domain.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRouteRepository {
    Page<HubRoute> findAllByDeletedAtIsNull(Pageable pageable);

}
