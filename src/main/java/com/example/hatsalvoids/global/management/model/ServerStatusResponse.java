package com.example.hatsalvoids.global.management.model;

public record ServerStatusResponse(
        String timestamp,
        double systemCpuUsage,
        double processCpuUsage,
        long heapUsedBytes,
        long heapMaxBytes,
        int threadCount,
        long uptimeMillis
) {
}
