package com.agromatik.cloud.infrastructure.mysql.repository;

import com.agromatik.cloud.domain.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringTokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenValue(String tokenValue);
    boolean existsByTokenValueAndRevokedAtIsNull(String tokenValue);
}
