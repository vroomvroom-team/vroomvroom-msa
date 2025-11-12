package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.hub.application.command.CreateHubRouteCommand;
import com.vroomvroom.hub.application.command.UpdateHubRouteCommand;
import com.vroomvroom.hub.application.dto.HubRouteDetailRes;
import com.vroomvroom.hub.application.dto.HubRouteListRes;
import com.vroomvroom.hub.application.dto.OptimalRouteRes;
import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.repository.HubRepository;
import com.vroomvroom.hub.domain.repository.HubRouteFindRepository;
import com.vroomvroom.hub.domain.service.OptimalRouteType;
import com.vroomvroom.hub.exception.HubErrorCode;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRouteRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRouteServiceImpl implements HubRouteService {

    private final HubRepository hubRepository;
    private final HubRouteFindRepository hubRouteFindRepository;
    private final HubConnection hubConnection;

    @Override
    @Transactional
    public CreateHubRouteRes createHubRoute(CreateHubRouteCommand command) {
        Hub departure = findHubById(command.getDepartureHubId());
        Hub arrival = findHubById(command.getArrivalHubId());
        log.info("departureHub: {}, arrivalHub: {}", departure.getHubName(), arrival.getHubName());
        validateHub(departure, arrival);
        HubRoute hubRoute = HubRoute.of(
                command.getRouteName(),
                departure,
                arrival,
                command.getTime(),
                command.getDistance()
        );
        departure.createRoute(hubRoute);
        hubRepository.save(departure);
        return CreateHubRouteRes.from(hubRoute);
    }

    @Override
    public PageResponse<HubRouteListRes> getHubRouteList(Pageable pageable) {
        Page<HubRoute> hubRoutes = hubRouteFindRepository.findAllWithHubs(pageable);
        return PageResponse.fromPage(hubRoutes.map(HubRouteListRes::from));
    }

    @Override
    @Cacheable(cacheNames = "hubRouteCache", key = "#routeId")
    public HubRouteDetailRes getHubRouteDetail(UUID routeId) {
        HubRoute route = findHubRouteById(routeId);
        return HubRouteDetailRes.from(route);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"hubRouteCache", "optimalRouteCache"}, allEntries = true)
    public void updateHubRoute(UUID routeId, UpdateHubRouteCommand command) {
        HubRoute hubRoute = findHubRouteById(routeId);
        hubRoute.update(command.getRouteName(), command.getTime(), command.getDistance(), command.getIsActive());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"hubRouteCache", "optimalRouteCache"}, allEntries = true)
    public void deleteHubRoute(UUID routeId) {
        HubRoute hubRoute = findHubRouteById(routeId);
        Hub departure = hubRoute.getDepartureHub();
        departure.removeRoute(hubRoute);
        hubRepository.save(departure);
    }

    @Override
    @Cacheable(cacheNames = "optimalRouteCache", key = "#departureId.toString() + '-' + #arrivalId.toString() + '-' + #type")
    public OptimalRouteRes findOptimalPath(UUID departureId, UUID arrivalId, OptimalRouteType type) {
        Hub departure = findHubById(departureId);
        Hub arrival = findHubById(arrivalId);
        if (departure.getHubId().equals(arrival.getHubId())) throw new CustomException(HubErrorCode.SAME_DEPARTURE_ARRIVAL_HUB);
        List<HubRoute> allRoutes = hubRouteFindRepository.findAllActive();
        if (allRoutes.isEmpty()) throw new CustomException(HubErrorCode.NO_ACTIVE_ROUTE);
        Map<UUID, List<HubRoute>> graph = allRoutes.stream()
                .collect(Collectors.groupingBy(r -> r.getDepartureHub().getHubId()));
        log.info("최적 경로 탐색 준비 - 출발: {} -> 도착: {}", departure.getHubName(), arrival.getHubName());
        OptimalRouteRes res = Dijkstra.findOptimalRoute(graph, departureId, arrivalId, type);
        log.info("최적 경로 탐색 완료 - 출발: {} -> 도착: {}, 경유지 수: {}, 총 비용: {}", departure.getHubName(), arrival.getHubName(), res.getPath().size(), res.getTotalCost());
        return res;
    }

    private Hub findHubById(UUID hubId) {
        return hubRepository.findHubByHubIdAndDeletedAtIsNull(hubId)
                .orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));
    }

    private HubRoute findHubRouteById(UUID routeId) {
        return hubRouteFindRepository.findHubRouteWithHubsByRouteId(routeId)
                .orElseThrow(() -> new CustomException(HubErrorCode.HUB_ROUTE_NOT_FOUND));
    }

    private void validateHub(Hub departure, Hub arrival) {
        if (departure.getHubId().equals(arrival.getHubId())) throw new CustomException(HubErrorCode.SAME_DEPARTURE_ARRIVAL_HUB);
        if (!hubConnection.isConnected(departure.getHubName(), arrival.getHubName())) throw new CustomException(HubErrorCode.HUBS_NOT_CONNECTED);
    }
}