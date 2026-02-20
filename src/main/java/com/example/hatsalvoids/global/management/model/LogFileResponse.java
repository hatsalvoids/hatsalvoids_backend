package com.example.hatsalvoids.global.management.model;

import java.util.List;

public record LogFileResponse(
        String filePath,
        int lines,
        List<String> content
) {
}
