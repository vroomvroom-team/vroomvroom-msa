package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.application.command.UpdateHubCommand;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.repository.HubRepository;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.ErrorCode;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
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
        Hub hub = Hub.of(
                command.getHubName(),
                command.getAddress(),
                command.getLatitude(),
                command.getLongitude()
        );
        return CreateHubRes.from(hubRepository.save(hub));
    }

    @Override
    public PageResponse<HubListRes> getHubList(Pageable pageable) {
        Page<Hub> hubs = hubRepository.findAllByDeletedAtIsNull(pageable);
        return PageResponse.fromPage(hubs.map(HubListRes::from));
    }

    @Override
    public HubDetailRes getHubDetail(UUID hubId) {
        Hub hub = findHubById(hubId);
        return HubDetailRes.from(hub);
    }

    @Override
    @Transactional
    public void updateHub(UUID hubId, UpdateHubCommand command) {
        Hub hub = findHubById(hubId);
        hub.update(command.getHubName(), command.getAddress(), command.getLatitude(), command.getLongitude());
    }

    @Override
    @Transactional
    public void deleteHub(UUID hubId) {
        Hub hub = findHubById(hubId);
        hub.markAsDeleted();
    }

    Hub findHubById(UUID hubId) {
        return hubRepository.findHubByHubId(hubId)
                .orElseThrow(() -> new CustomException(ErrorCode.HUB_NOT_FOUND));
    }
}
