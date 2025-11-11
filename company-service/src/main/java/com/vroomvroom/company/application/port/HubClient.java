package com.vroomvroom.company.application.port;

import java.util.UUID;

public interface HubClient {
    boolean existsHub(UUID hubId);
    boolean existsHubManager(UUID hubId, Long userId);
}
