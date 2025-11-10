package com.vroomvroom.delivery.domain.event;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerAssignmentEvent {

    private UUID routeId;

    public ManagerAssignmentEvent(UUID routeId) {
        this.routeId = routeId;
    }
}
