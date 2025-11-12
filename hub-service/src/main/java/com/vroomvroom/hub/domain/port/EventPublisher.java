package com.vroomvroom.hub.domain.port;

import com.vroomvroom.hub.domain.event.DomainEvent;

public interface EventPublisher {
    void publish(String topic, DomainEvent event);
}
