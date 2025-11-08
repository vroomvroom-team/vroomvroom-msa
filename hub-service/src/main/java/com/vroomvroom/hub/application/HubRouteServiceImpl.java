package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.dto.HubRouteListRes;
import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubRouteServiceImpl implements HubRouteService {

    private final HubRouteRepository hubRouteRepository;

    @Override
    public PageResponse<HubRouteListRes> getHubRouteList(Pageable pageable) {
        Page<HubRoute> hubRoutes = hubRouteRepository.findAllByDeletedAtIsNull(pageable);
        return PageResponse.fromPage(hubRoutes.map(HubRouteListRes::from));
    }
}
