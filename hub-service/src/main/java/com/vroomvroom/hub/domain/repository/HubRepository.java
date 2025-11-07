package com.vroomvroom.hub.domain.repository;

import com.vroomvroom.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {

    Hub save(Hub hub);
    boolean existsByHubName(String hubName);
    Page<Hub> findAllByDeletedAtIsNull(Pageable pageable);
    Optional<Hub> findHubByHubId(UUID hubId);
}
