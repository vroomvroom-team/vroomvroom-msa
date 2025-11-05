package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {
}
