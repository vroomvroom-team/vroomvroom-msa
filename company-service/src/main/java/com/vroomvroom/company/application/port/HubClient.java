package com.vroomvroom.company.application.port;

import com.vroomvroom.company.domain.vo.HubId;

public interface HubClient {
    boolean existsHub(HubId hubId);
}
