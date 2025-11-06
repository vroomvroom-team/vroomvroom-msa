package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {
    boolean existsByHubName(String hubName);
    Page<Hub> findAllByDeletedAtIsNull(Pageable pageable);
}
