package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {

    private final JpaHubRepository jpaHubRepository;

    @Override
    public Hub save(Hub hub) {
        return jpaHubRepository.save(hub);
    }

    @Override
    public boolean existsByHubName(String hubName) {
        return jpaHubRepository.existsByHubName(hubName);
    }

    @Override
    public Page<Hub> findAllByDeletedAtIsNull(Pageable pageable) {
        return jpaHubRepository.findAllByDeletedAtIsNull(pageable);
    }

    @Override
    public Optional<Hub> findHubByHubIdAndDeletedAtIsNull(UUID hubId) {
        return jpaHubRepository.findHubByHubIdAndDeletedAtIsNull(hubId);
    }

    @Override
    public boolean existsById(UUID hubId) {
        return jpaHubRepository.existsById(hubId);
    }

    @Override
    public Optional<Long> findHubManagerIdByHubId(UUID hubId) {
        return jpaHubRepository.findHubManagerIdByHubId(hubId);
    }
}