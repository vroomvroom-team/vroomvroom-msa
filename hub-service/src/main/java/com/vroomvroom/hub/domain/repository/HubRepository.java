package com.vroomvroom.hub.domain.repository;

import com.vroomvroom.hub.domain.entity.Hub;

import java.util.Optional;

public interface HubRepository {

    Hub save(Hub hub);
    boolean existsByHubName(String hubName);
}
