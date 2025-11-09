package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.command.CreateHubRouteCommand;
import com.vroomvroom.hub.application.dto.HubRouteDetailRes;
import com.vroomvroom.hub.application.dto.HubRouteListRes;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRouteRes;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HubRouteService {

    CreateHubRouteRes createHubRoute(CreateHubRouteCommand command);
    PageResponse<HubRouteListRes> getHubRouteList(Pageable pageable);
    HubRouteDetailRes getHubRouteDetail(UUID routeId);
}
