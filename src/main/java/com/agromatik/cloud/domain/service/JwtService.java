package com.agromatik.cloud.domain.service;
import com.agromatik.cloud.application.port.out.TokenPort;
import com.agromatik.cloud.domain.model.Token;
import com.agromatik.cloud.infrastructure.mysql.repository.SpringTokenRepository;
import io.jsonwebtoken.security.Keys;

import com.agromatik.cloud.domain.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    private final Key secretKey;
    private final long expiration;
    private final TokenPort tokenPort;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration,
            TokenPort tokenPort) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
        this.tokenPort = tokenPort;
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
        tokenPort.save(tokenEntity);

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
        return tokenPort.existsByTokenValueAndRevokedAtIsNull(token);
    }

    public void revokeToken(String token) {
        tokenPort.findByTokenValue(token).ifPresent(t -> {
            t.setRevokedAt(LocalDateTime.now());
            tokenPort.save(t);
        });
    }
}