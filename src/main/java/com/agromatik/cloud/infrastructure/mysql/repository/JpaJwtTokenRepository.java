package com.agromatik.cloud.infrastructure.mysql.repository;

import com.agromatik.cloud.application.port.out.JwtTokenRepositoryPort;
import com.agromatik.cloud.domain.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaJwtTokenRepository extends JpaRepository<JwtToken, Long>, JwtTokenRepositoryPort {
    Optional<JwtToken> findByToken(String token);
}
