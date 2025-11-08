package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.dto.HubRouteListRes;
import org.springframework.data.domain.Pageable;

public interface HubRouteService {

    PageResponse<HubRouteListRes> getHubRouteList(Pageable pageable);
}
