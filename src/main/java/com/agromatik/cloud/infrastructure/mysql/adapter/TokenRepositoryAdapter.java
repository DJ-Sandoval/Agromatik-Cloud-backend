package com.agromatik.cloud.infrastructure.mysql.adapter;

import com.agromatik.cloud.application.port.out.TokenPort;
import com.agromatik.cloud.domain.model.Token;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenPort {
    private final SpringTokenRepository springTokenRepository;

    @Override
    public Token save(Token token) {
        return springTokenRepository.save(token);
    }

    @Override
    public Optional<Token> findByTokenValue(String tokenValue) {
        return springTokenRepository.findByTokenValue(tokenValue);
    }

    @Override
    public boolean existsByTokenValueAndRevokedAtIsNull(String tokenValue) {
        return springTokenRepository.existsByTokenValueAndRevokedAtIsNull(tokenValue);
    }

    @Override
    public void revokeToken(Token token) {
        token.setRevokedAt(LocalDateTime.now());
        springTokenRepository.save(token);
    }
}
