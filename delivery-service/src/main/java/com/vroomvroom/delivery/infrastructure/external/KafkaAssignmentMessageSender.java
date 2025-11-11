package com.vroomvroom.delivery.infrastructure.external;

import com.vroomvroom.delivery.domain.event.ManagerAssignmentEvent;
import com.vroomvroom.delivery.domain.port.DeliveryAssignmentMessageSender;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAssignmentMessageSender implements DeliveryAssignmentMessageSender {

    @Value("${app.kafka.topics.manager-assignment}")
    private String topicName;

    private final KafkaTemplate<String, ManagerAssignmentEvent> kafkaTemplate;

    @Override
    public void send(UUID deliveryId, ManagerAssignmentEvent event) {
        kafkaTemplate.send(topicName, deliveryId.toString(), event);
    }
}
