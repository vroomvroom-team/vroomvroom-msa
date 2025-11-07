package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.repository.HubRepository;
import com.vroomvroom.hub.domain.vo.Location;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;

    @Override
    @Transactional
    public CreateHubRes createHub(CreateHubCommand command) {
        if (hubRepository.existsByHubName(command.getHubName())) throw new CustomException(ErrorCode.DUPLICATE_HUB_NAME);
        Hub hub = Hub.builder()
                .hubName(command.getHubName())
                .address(command.getAddress())
                .location(new Location(command.getLatitude(), command.getLongitude()))
                .build();
        return CreateHubRes.from(hubRepository.save(hub));
    }

    @Override
    public PageResponse<HubListRes> getHubList(int page, int size, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        Page<Hub> hubs = hubRepository.findAllByDeletedAtIsNull(pageable);
        return PageResponse.fromPage(hubs.map(HubListRes::from));
    }

    @Override
    public HubDetailRes getHubDetail(UUID hubId) {
        Hub hub = hubRepository.findHubByHubId(hubId);
        if (hub == null) throw new CustomException(ErrorCode.HUB_NOT_FOUND);
        return HubDetailRes.from(hub);
    }
}
