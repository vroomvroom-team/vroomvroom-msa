package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.command.CreateHubRouteCommand;
import com.vroomvroom.hub.application.dto.HubRouteDetailRes;
import com.vroomvroom.hub.application.dto.HubRouteListRes;
import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.repository.HubRepository;
import com.vroomvroom.hub.domain.repository.HubRouteRepository;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.ErrorCode;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRouteRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRouteServiceImpl implements HubRouteService {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;

    @Override
    @Transactional
    public CreateHubRouteRes createHubRoute(CreateHubRouteCommand command) {
        Hub departure = findHubById(command.getDepartureHubId());
        Hub arrival = findHubById(command.getArrivalHubId());
        log.info("👍👍👍👍👍👍👍 departureHub: {}, arrivalHub: {}", departure.getHubName(), arrival.getHubName());
        validateHub(departure, arrival);
        HubRoute hubRoute = HubRoute.of(
                departure,
                arrival,
                command.getTime(),
                command.getDistance()
        );
        return CreateHubRouteRes.from(hubRouteRepository.save(hubRoute));
    }

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

    private Hub findHubById(UUID hubId) {
        return hubRepository.findHubByHubId(hubId)
                .orElseThrow(() -> new CustomException(ErrorCode.HUB_NOT_FOUND));
    }

    private HubRoute findHubRouteById(UUID routeId) {
        return hubRouteRepository.findHubRouteWithHubsByRouteId(routeId)
                .orElseThrow(() -> new CustomException(ErrorCode.HUB_ROUTE_NOT_FOUND));
    }

    private void validateHub(Hub departure, Hub arrival) {
        if (departure.getHubId().equals(arrival.getHubId())) throw new CustomException(ErrorCode.SAME_DEPARTURE_ARRIVAL_HUB);
        if (hubRouteRepository.existsByDepartureHub_HubIdAndArrivalHub_HubId(departure.getHubId(), departure.getHubId())) {
            throw new CustomException(ErrorCode.DUPLICATE_HUB_ROUTE);
        }
        if (departure.getHubZone() != arrival.getHubZone()) throw new CustomException(ErrorCode.HUBS_NOT_CONNECTED);
    }
}
