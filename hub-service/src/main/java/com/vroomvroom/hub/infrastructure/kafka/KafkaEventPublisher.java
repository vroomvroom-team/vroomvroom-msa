package com.vroomvroom.hub.infrastructure.kafka;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.hub.domain.event.HubDomainEvent;
import com.vroomvroom.hub.domain.port.EventPublisher;
import com.vroomvroom.hub.exception.HubErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(String topic, HubDomainEvent event) {
        try {
            kafkaTemplate.send(topic, event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("이벤트 발행 실패 - Topic: {}, Event: {}", topic, event, ex);
                        } else {
                            log.info("이벤트 발행 성공 - Topic: {}, Partition: {}, Offset: {}",
                                    topic, result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Kafka 이벤트 발행 중 예외 발생", e);
            throw new CustomException(HubErrorCode.EVENT_PUBLISH_FAILURE);
        }
    }
}
