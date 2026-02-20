package com.example.hatsalvoids.global.security;

public record RequestIdentity(
        String clientIp,
        String forwardedFor,
        String realIp,
        String userAgent,
        String referer,
        String origin,
        String subject,
        String userId,
        String email,
        String role,
        String tokenHash
) {
}
