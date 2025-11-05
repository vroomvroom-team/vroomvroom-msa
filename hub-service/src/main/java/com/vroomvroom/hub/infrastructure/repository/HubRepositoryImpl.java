package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final JpaHubRepository jpaHubRepository;

    @Override
    public Hub save(Hub hub) {
        return jpaHubRepository.save(hub);
    }
}
