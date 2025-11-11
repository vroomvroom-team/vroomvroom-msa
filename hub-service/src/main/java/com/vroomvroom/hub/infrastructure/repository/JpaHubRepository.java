package com.vroomvroom.hub.infrastructure.repository;

import com.vroomvroom.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {
    boolean existsByHubName(String hubName);
    Page<Hub> findAllByDeletedAtIsNull(Pageable pageable);
    Optional<Hub> findHubByHubIdAndDeletedAtIsNull(UUID hubId);

    @Query("SELECT h.hubManagerId FROM Hub h " +
            "WHERE h.hubId = :hubId AND " +
            "h.deletedAt IS NULL")
    Optional<Long> findHubManagerIdByHubId(@Param("hubId") UUID hubId);
}