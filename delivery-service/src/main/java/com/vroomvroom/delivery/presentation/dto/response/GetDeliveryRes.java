package com.vroomvroom.delivery.presentation.dto.response;


import com.vroomvroom.delivery.domain.entity.Delivery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class GetDeliveryRes {

    private UUID deliveryId;
    private UUID orderId;
    private UUID startHubId;
    private UUID arriveHubId;
    private Long receiverId;
    private UUID receiverSlackId;
    private List<GetDeliveryRoutesRes> deliveryRouteRes;
    private LocalDateTime startTime;
    private LocalDateTime arriveTime;
    private String address;
    private String status;

    public GetDeliveryRes(
        UUID deliveryId, UUID orderId, UUID startHubId, UUID arriveHubId,
        Long receiverId, UUID receiverSlackId, List<GetDeliveryRoutesRes> deliveryRouteRes,
        LocalDateTime startTime, LocalDateTime arriveTime, String address, String status
    ) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.startHubId = startHubId;
        this.arriveHubId = arriveHubId;
        this.receiverId = receiverId;
        this.receiverSlackId = receiverSlackId;
        this.deliveryRouteRes = deliveryRouteRes;
        this.startTime = startTime;
        this.arriveTime = arriveTime;
        this.address = address;
        this.status = status;
    }

    public static GetDeliveryRes from(Delivery delivery) {
        return new GetDeliveryRes(
            delivery.getId(),
            delivery.getOrderId().getId(),
            delivery.getStartHubId().getId(),
            delivery.getArriveHubId().getId(),
            delivery.getReceiverId().getId(),
            delivery.getReceiverSlackId().getId(),
            GetDeliveryRoutesRes.from(delivery.getDeliveryRoutes()),
            delivery.getStartTime(),
            delivery.getArriveTime(),
            delivery.getDeliveryAddress().getAddress(),
            delivery.getStatus().name()
        );
    }
}
