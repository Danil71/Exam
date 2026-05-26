package com.software.software_development.core.utility;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.software.software_development.core.configuration.AppConfigurationProperties;
import com.software.software_development.model.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtUtils {

    private final AppConfigurationProperties appConfigurationProperties;

    public String extractLogin(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(final String token) {
        Object email = extractAllClaims(token).get("email");
        return email != null ? email.toString() : null;
    }

    public Long extractUserId(final String token) {
        Object userIdClaim = extractAllClaims(token).get("user_id");
        if (userIdClaim == null) {
            return null;
        }
        if (userIdClaim instanceof Number) {
            return ((Number) userIdClaim).longValue();
        }
        if (userIdClaim instanceof String) {
            return Long.parseLong((String) userIdClaim);
        }
        throw new IllegalArgumentException("Invalid user_id type in token");
    }

    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        String secretKey = appConfigurationProperties.getJwt().getSecretKey();
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(final UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("account_creation_timestamp", user.getCreatedAt().toString());
        claims.put("user_id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        return createToken(claims, user.getLogin(), TimeUnit.MINUTES.toMillis(30));
    }

    public String generateRefreshToken(final UserEntity user) {
        return createToken(new HashMap<>(), user.getLogin(), TimeUnit.DAYS.toMillis(30));
    }

    private String createToken(final Map<String, Object> claims, final String subject, final Long expiration) {
        String secretKey = appConfigurationProperties.getJwt().getSecretKey();
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(final String token, final UserDetails userDetails) {
        Long userId = extractUserId(token);
        if (userId == null) {
            return false;
        }
        return userId.toString().equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
