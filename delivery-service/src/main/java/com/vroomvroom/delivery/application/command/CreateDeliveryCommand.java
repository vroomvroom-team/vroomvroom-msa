package com.vroomvroom.delivery.application.command;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.vo.ArriveHubId;
import com.vroomvroom.delivery.domain.vo.DeliveryAddress;
import com.vroomvroom.delivery.domain.vo.ReceiverId;
import com.vroomvroom.delivery.domain.vo.ReceiverSlackId;
import com.vroomvroom.delivery.domain.vo.StartHubId;
import com.vroomvroom.delivery.presentation.dto.request.CreateDeliveryReq;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CreateDeliveryCommand {

    private UUID orderId;
    private StartHubId startHubId; // 공급업체 허브 ID
    private ArriveHubId arriveHubId; // 수령업체 허브 ID
    private ReceiverId receiverId;
    private ReceiverSlackId receiverSlackId;
    private DeliveryAddress address;

    private CreateDeliveryCommand(
        UUID orderId, StartHubId startHubId, ArriveHubId arriveHubId,
        ReceiverId receiverId, ReceiverSlackId receiverSlackId, DeliveryAddress address
    ) {

        if (startHubId.getId().equals(arriveHubId.getId())) {
            throw new CustomException(DeliveryErrorCode.START_HUB_EQUALS_ARRIVE_HUB);
        }

        this.orderId = orderId;
        this.startHubId = startHubId;
        this.arriveHubId = arriveHubId;
        this.receiverId = receiverId;
        this.receiverSlackId = receiverSlackId;
        this.address = address;
    }

    public static CreateDeliveryCommand from(CreateDeliveryReq request) {
        return new CreateDeliveryCommand(
            request.getOrderId(),
            StartHubId.of(request.getStartHubId()),
            ArriveHubId.of(request.getArriveHubId()),
            ReceiverId.of(request.getReceiverId()),
            ReceiverSlackId.of(request.getReceiverSlackId()),
            DeliveryAddress.of(request.getAddress())
        );
    }
}
