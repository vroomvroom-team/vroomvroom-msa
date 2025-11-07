package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.presentation.dto.request.UpdateHubReq;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface HubService {

    CreateHubRes createHub(CreateHubCommand command);
    PageResponse<HubListRes> getHubList(int page, int size, Sort.Direction direction);
    HubDetailRes getHubDetail(UUID hubId);
    void updateHub(UUID hubId, UpdateHubReq req);
}
