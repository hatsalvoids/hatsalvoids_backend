package com.example.hatsalvoids.global.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequestIdentityResolver {

    private final TokenClaimExtractor tokenClaimExtractor;

    public String resolveIdentifier(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        Optional<String> userId = tokenClaimExtractor.extractUserId(authorization);
        Optional<String> email = tokenClaimExtractor.extractEmail(authorization);
        Optional<String> subject = tokenClaimExtractor.extractSubject(authorization);
        Optional<String> tokenHash = tokenClaimExtractor.extractTokenHash(authorization);

        if (userId.isPresent()) {
            return "userId:" + userId.get();
        }
        if (email.isPresent()) {
            return "email:" + email.get();
        }
        if (subject.isPresent()) {
            return "sub:" + subject.get();
        }
        if (tokenHash.isPresent()) {
            return "token:" + tokenHash.get();
        }
        return "ip:" + resolveClientIp(request);
    }

    public RequestIdentity resolveIdentity(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        return new RequestIdentity(
                resolveClientIp(request),
                request.getHeader("X-Forwarded-For"),
                request.getHeader("X-Real-IP"),
                request.getHeader(HttpHeaders.USER_AGENT),
                request.getHeader(HttpHeaders.REFERER),
                request.getHeader(HttpHeaders.ORIGIN),
                tokenClaimExtractor.extractSubject(authorization).orElse(null),
                tokenClaimExtractor.extractUserId(authorization).orElse(null),
                tokenClaimExtractor.extractEmail(authorization).orElse(null),
                tokenClaimExtractor.extractRole(authorization).orElse(null),
                tokenClaimExtractor.extractTokenHash(authorization).orElse(null)
        );
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}
