package com.agromatik.cloud.application.port.out;

import com.agromatik.cloud.domain.model.JwtToken;
import java.util.Optional;

public interface JwtTokenRepositoryPort {
    JwtToken save(JwtToken token);
    Optional<JwtToken> findByToken(String token);
}
