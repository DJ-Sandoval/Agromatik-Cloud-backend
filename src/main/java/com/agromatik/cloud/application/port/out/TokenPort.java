package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.Token;

import java.util.Optional;

public interface TokenPort {
    Token save(Token token);
    Optional<Token> findByTokenValue(String tokenValue);
    boolean existsByTokenValueAndRevokedAtIsNull(String tokenValue);
    void revokeToken(Token token);
}
