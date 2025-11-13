package com.vroomvroom.orderservice.application.event;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.orderservice.domain.event.OrderCreatedEvent;
import com.vroomvroom.orderservice.domain.port.OrderEventPublisher;
import com.vroomvroom.orderservice.exception.OrderErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventHandler implements OrderEventPublisher {

    @Value("${app.kafka.topics.order-created}")
    private String orderCreatedTopic;

    private final KafkaTemplate<String, OrderCreatedEvent> orderEventKafkaTemplate;

    @Override
    public void publishOrderCreated(UUID orderId, OrderCreatedEvent event) {
        try {
            log.info("주문 생성 이벤트 시작 - orderId={}", orderId);

            CompletableFuture<SendResult<String, OrderCreatedEvent>> future =
                    orderEventKafkaTemplate.send(orderCreatedTopic, orderId.toString(), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("주문 생성 이벤트 발행 성공 - orderId: {}, partition: {}, offset: {}",
                            orderId,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("주문 생성 이벤트 발행 실패 - orderId: {}", orderId, ex);
                    // 실패 시 보상 트랜잭션 또는 재시도 로직 필요
                }
            });
        } catch (Exception e) {
            log.error("주문 생성 이벤트 발행 중 예외 발생 - orderId: {}", orderId, e);
            throw new CustomException(OrderErrorCode.KAFKA_RUNTIME_EXCEPTION);
        }
    }
}
