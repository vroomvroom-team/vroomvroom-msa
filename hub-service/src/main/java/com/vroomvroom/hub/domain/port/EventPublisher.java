package com.vroomvroom.hub.domain.port;

import com.vroomvroom.hub.domain.event.HubDomainEvent;

public interface EventPublisher {
    void publish(String topic, HubDomainEvent event);
}
