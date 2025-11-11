package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.command.*;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.application.dto.HubManagerRes;
import com.vroomvroom.hub.application.dto.StockRes;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import com.vroomvroom.hub.presentation.dto.response.CreateStockRes;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface HubService {

    CreateHubRes createHub(CreateHubCommand command);
    PageResponse<HubListRes> getHubList(Pageable pageable);
    HubDetailRes getHubDetail(UUID hubId);
    void updateHub(UUID hubId, UpdateHubCommand command);
    void deleteHub(UUID hubId);
    boolean existsHub(UUID hubId);
    HubManagerRes getHubManager(UUID hubId);
    CreateStockRes createStock(CreateStockCommand command);
    List<StockRes> getStockList(UUID hubId);
    void decreaseStock(DecreaseStockCommand command);
    void increaseStock(IncreaseStockCommand command);
    StockRes getStock(UUID hubId, UUID productId);
}