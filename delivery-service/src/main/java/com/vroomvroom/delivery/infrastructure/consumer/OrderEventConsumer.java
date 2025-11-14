package com.vroomvroom.delivery.infrastructure.consumer;

import com.vroomvroom.delivery.application.command.CreateDeliveryCommand;
import com.vroomvroom.delivery.application.service.DeliveryService;
import com.vroomvroom.delivery.domain.event.OrderCreatedEvent;
import com.vroomvroom.delivery.domain.port.UserClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final DeliveryService deliveryService;
    private final UserClient userClient;

    @KafkaListener(
        topics = "${app.kafka.topics.order.created}",
        groupId = "${app.kafka.consumer.group-id}",
        containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    @Transactional
    public void listenOrderCreated(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent: {}", event);

        try {
            UUID userSlackId = userClient.getUserSlackId(event.getReceiverId());
            CreateDeliveryCommand command = CreateDeliveryCommand.of(event, userSlackId);
            deliveryService.createDelivery(command);
        } catch (Exception e) {
            log.error("OrderCreatedEvent를 처리하는 중 오류가 발생했습니다. event = {}", event, e);
            throw e;
        }
    }
}
