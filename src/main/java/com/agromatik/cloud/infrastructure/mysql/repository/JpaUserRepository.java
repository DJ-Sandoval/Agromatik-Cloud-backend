package com.agromatik.cloud.infrastructure.mysql.repository;

import com.agromatik.cloud.application.port.out.UserRepositoryPort;
import com.agromatik.cloud.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepositoryPort {
    Optional<User> findByUsername(String username);
}
