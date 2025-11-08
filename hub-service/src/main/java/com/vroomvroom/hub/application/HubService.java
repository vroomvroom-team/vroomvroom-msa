package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.application.command.UpdateHubCommand;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface HubService {

    CreateHubRes createHub(CreateHubCommand command);
    PageResponse<HubListRes> getHubList(Pageable pageable);
    HubDetailRes getHubDetail(UUID hubId);
    void updateHub(UUID hubId, UpdateHubCommand command);
    void deleteHub(UUID hubId);
}
