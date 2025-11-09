package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.dto.HubRouteDetailRes;
import com.vroomvroom.hub.application.dto.HubRouteListRes;
import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.repository.HubRouteRepository;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteServiceImpl implements HubRouteService {

    private final HubRouteRepository hubRouteRepository;

    @Override
    public PageResponse<HubRouteListRes> getHubRouteList(Pageable pageable) {
        Page<HubRoute> hubRoutes = hubRouteRepository.findAllWithHubs(pageable);
        return PageResponse.fromPage(hubRoutes.map(HubRouteListRes::from));
    }

    @Override
    public HubRouteDetailRes getHubRouteDetail(UUID routeId) {
        HubRoute route = findHubRouteById(routeId);
        return HubRouteDetailRes.from(route);
    }

    HubRoute findHubRouteById(UUID routeId) {
        return hubRouteRepository.findHubRouteWithHubsByRouteId(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.HUB_ROUTE_NOT_FOUND));
    }
}
