package com.vroomvroom.user.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.vroomvroom.user.domain.entity.User;
import com.vroomvroom.user.domain.model.UserStatus;
import com.vroomvroom.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final JpaUserRepository jpaUserRepository;

	@Override
	public User save(User user) {
		return jpaUserRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return jpaUserRepository.findByEmail(email);
	}

	@Override
	public Optional<User> findById(Long id) {
		return jpaUserRepository.findById(id);
	}

	@Override
	public boolean existsByEmail(String email) {
		return jpaUserRepository.existsByEmail(email);
	}

	@Override
	public boolean existsById(Long id) {
		return jpaUserRepository.existsById(id);
	}

	@Override
	public Page<User> findAllByStatus(UserStatus status, Pageable pageable) {
		return jpaUserRepository.findAllByStatus(status, pageable);
	}
}