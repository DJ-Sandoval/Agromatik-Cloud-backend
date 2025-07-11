package com.agromatik.cloud.application.service;

import com.agromatik.cloud.application.port.out.JwtTokenRepositoryPort;
import com.agromatik.cloud.domain.model.JwtToken;
import com.agromatik.cloud.domain.model.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtTokenRepositoryPort jwtTokenRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setUser(user);
        jwtToken.setCreatedAt(LocalDateTime.now());
        jwtToken.setExpiresAt(LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
        jwtToken.setRevoked(false);
        jwtTokenRepository.save(jwtToken);

        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return jwtTokenRepository.findByToken(token)
                    .map(t -> !t.isRevoked())
                    .orElse(false);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
