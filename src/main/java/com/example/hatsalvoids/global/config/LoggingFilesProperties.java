package com.example.hatsalvoids.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logging.files")
public record LoggingFilesProperties(
        String app,
        String error
) {
}
