package com.example.hatsalvoids.global.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenClaimExtractor {

    private static final String BEARER_PREFIX = "Bearer ";

    private final ObjectMapper objectMapper;

    public Optional<Map<String, Object>> extractClaims(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        if (token == null) {
            return Optional.empty();
        }
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            return Optional.empty();
        }
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            String payload = new String(decoded, StandardCharsets.UTF_8);
            Map<String, Object> claims = objectMapper.readValue(payload, new TypeReference<>() {
            });
            return Optional.of(claims);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    public Optional<String> extractRole(String authorizationHeader) {
        return extractStringClaim(authorizationHeader, "role");
    }

    public Optional<String> extractSubject(String authorizationHeader) {
        return extractStringClaim(authorizationHeader, "sub");
    }

    public Optional<String> extractEmail(String authorizationHeader) {
        return extractStringClaim(authorizationHeader, "email");
    }

    public Optional<String> extractUserId(String authorizationHeader) {
        return extractStringClaim(authorizationHeader, "userId");
    }

    public Optional<String> extractTokenHash(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        if (token == null) {
            return Optional.empty();
        }
        return Optional.of(hashSha256(token));
    }

    private Optional<String> extractStringClaim(String authorizationHeader, String key) {
        return extractClaims(authorizationHeader)
                .map(claims -> Objects.toString(claims.get(key), null))
                .filter(value -> value != null && !value.isBlank());
    }

    public String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return null;
        }
        if (authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        }
        return authorizationHeader.trim();
    }

    private String hashSha256(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            return "hash-failed";
        }
    }
}
