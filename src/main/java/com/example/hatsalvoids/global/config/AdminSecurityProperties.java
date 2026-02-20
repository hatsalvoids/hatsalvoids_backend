package com.example.hatsalvoids.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.admin")
public record AdminSecurityProperties(
        String token,
        String tokenSecret
) {
}
