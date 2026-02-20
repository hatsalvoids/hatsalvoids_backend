package com.example.hatsalvoids.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rate-limit.shade")
public record ShadeRateLimitProperties(
        int maxRequests,
        long windowSeconds
) {
}
