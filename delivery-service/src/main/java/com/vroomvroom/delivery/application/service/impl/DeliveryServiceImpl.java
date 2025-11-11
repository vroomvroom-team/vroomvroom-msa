package com.vroomvroom.delivery.application.service.impl;

import com.vroomvroom.delivery.application.command.CreateDeliveryCommand;
import com.vroomvroom.delivery.application.dto.GetDeliveryRoutesReq;
import com.vroomvroom.delivery.application.service.DeliveryManagerService;
import com.vroomvroom.delivery.application.service.DeliveryService;
import com.vroomvroom.delivery.domain.entity.Delivery;
import com.vroomvroom.delivery.domain.entity.DeliveryRoute;
import com.vroomvroom.delivery.domain.port.HubClient;
import com.vroomvroom.delivery.domain.port.OrderClient;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import com.vroomvroom.delivery.domain.vo.ArriveHubId;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteSequence;
import com.vroomvroom.delivery.domain.vo.DeliveryRouteStatus;
import com.vroomvroom.delivery.domain.vo.OrderId;
import com.vroomvroom.delivery.domain.vo.StartHubId;
import com.vroomvroom.delivery.infrastructure.external.dto.HubRouteDTO;
import com.vroomvroom.delivery.presentation.dto.response.CreateDeliveryRes;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final OrderClient orderClient;
    private final HubClient hubClient;
    private final DeliveryManagerService deliveryManagerService;

    @Override
    @Transactional
    public CreateDeliveryRes createDelivery(CreateDeliveryCommand request) {
        // orderId 검증
        OrderId orderId = OrderId.of(request.getOrderId());
        orderClient.validateForDelivery(orderId.getId());

        // 허브-허브 경로들 가져오기
        List<HubRouteDTO> routes = hubClient.getRoutes(
            GetDeliveryRoutesReq.of(
                request.getStartHubId().getId(), request.getArriveHubId().getId()));

        // 경로 및 배송 생성
        List<DeliveryRoute> deliveryRoutes = createRoutesWithSequence(routes);
        Delivery delivery = Delivery.create(
            orderId, request.getStartHubId(), request.getArriveHubId(),
            deliveryRoutes, request.getAddress(),
            request.getReceiverId(), request.getReceiverSlackId()
        );
        deliveryRoutes.forEach(deliveryRoute ->
            deliveryRoute.attachToDelivery(delivery));

        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("배송 생성. deliveryId = {}", savedDelivery.getId());

        // 담당자 배정
        deliveryManagerService.assignManagerToDelivery(savedDelivery);

        return CreateDeliveryRes.from(savedDelivery);
    }

    private List<DeliveryRoute> createRoutesWithSequence(List<HubRouteDTO> routes) {
        List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

        int sequence = 0;
        for (HubRouteDTO route : routes) {
            DeliveryRoute routeEntity = DeliveryRoute.create(
                DeliveryRouteSequence.of((long) sequence++),
                StartHubId.of(route.getStartHubId()),
                ArriveHubId.of(route.getArriveHubId()),
                route.getDistance(),
                route.getTime(),
                DeliveryRouteStatus.HUB_MOVE_WAITING
            );
            deliveryRoutes.add(routeEntity);
        }

        return deliveryRoutes;
    }
}