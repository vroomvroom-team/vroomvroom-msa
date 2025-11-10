package com.vroomvroom.user.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.domain.model.UserStatus;

import lombok.NonNull;

public interface JpaUserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsById(@NonNull Long id);

	Page<User> findAllByStatus(UserStatus status, Pageable pageable);

}