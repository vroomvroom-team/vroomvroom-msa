package com.vroomvroom.delivery.domain.port;

import com.vroomvroom.delivery.domain.event.ManagerAssignmentEvent;
import java.util.UUID;

public interface DeliveryAssignmentMessageSender {

    void send(UUID deliveryId, ManagerAssignmentEvent event);
}
