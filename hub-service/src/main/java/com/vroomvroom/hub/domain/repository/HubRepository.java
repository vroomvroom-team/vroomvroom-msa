package com.vroomvroom.hub.domain.repository;

import com.vroomvroom.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HubRepository {

    Hub save(Hub hub);
    boolean existsByHubName(String hubName);
    Page<Hub> findAllByDeletedAtIsNull(Pageable pageable);
}
