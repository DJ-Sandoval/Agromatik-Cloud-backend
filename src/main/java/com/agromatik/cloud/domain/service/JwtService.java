package com.agromatik.cloud.domain.service;
import com.agromatik.cloud.application.port.out.TokenRepository;
import com.agromatik.cloud.domain.model.Token;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import com.agromatik.cloud.domain.model.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
public class JwtService {

    private final Key secretKey;
    private final long expiration;
    private final TokenRepository tokenRepository;

    public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration, TokenRepository tokenRepository) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusSeconds(expiration / 1000);

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        Token tokenEntity = Token.builder()
                .tokenValue(token)
                .user(user)
                .issuedAt(now)
                .expiresAt(expiryDate)
                .build();
        tokenRepository.save(tokenEntity);

        return token;
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        return tokenRepository.existsByTokenValueAndRevokedAtIsNull(token);
    }

    public void revokeToken(String token) {
        tokenRepository.findByTokenValue(token).ifPresent(t -> {
            t.setRevokedAt(LocalDateTime.now());
            tokenRepository.save(t);
        });
    }
}