package com.vroomvroom.hub.application;

import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.application.dto.HubRes;
import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.repository.HubRepository;
import com.vroomvroom.hub.domain.vo.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    @Transactional
    public HubRes.CreateHubRes createHub(CreateHubCommand command) {
        if (hubRepository.existsByHubName(command.getHubName())) throw new IllegalArgumentException("중복된 허브 이름입니다."); // TODO: ErrorCode 공통 적용
        Hub hub = Hub.builder()
                .hubName(command.getHubName())
                .address(command.getAddress())
                .location(new Location(command.getLatitude(), command.getLongitude()))
                .build();
        return HubRes.CreateHubRes.from(hubRepository.save(hub));
    }


}
