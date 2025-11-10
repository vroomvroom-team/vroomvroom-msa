package com.vroomvroom.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.domain.model.UserStatus;

public interface UserRepository {

	User save(User user);

	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

	boolean existsByEmail(String email);

	boolean existsById(Long id);

	Page<User> findAllByStatus(UserStatus status, Pageable pageable);

}