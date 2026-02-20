package com.example.hatsalvoids.global.management;

import com.example.hatsalvoids.global.config.LoggingFilesProperties;
import com.example.hatsalvoids.global.error.handler.GlobalErrorCode;
import com.example.hatsalvoids.global.management.model.LogFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogFileService {

    private static final int MAX_LINES = 2000;
    private static final int DEFAULT_LINES = 200;

    private final LoggingFilesProperties loggingFilesProperties;

    public LogFileResponse readErrorLog(Integer lines) {
        return readLogFile(loggingFilesProperties.error(), normalizeLines(lines));
    }

    public LogFileResponse readAppLog(Integer lines) {
        return readLogFile(loggingFilesProperties.app(), normalizeLines(lines));
    }

    private LogFileResponse readLogFile(String filePath, int lines) {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            throw new ManagementException(GlobalErrorCode.RESOURCE_NOT_FOUND);
        }
        List<String> content = readTail(path, lines);
        return new LogFileResponse(path.toAbsolutePath().toString(), lines, content);
    }

    private int normalizeLines(Integer lines) {
        if (lines == null) {
            return DEFAULT_LINES;
        }
        if (lines < 1) {
            return DEFAULT_LINES;
        }
        return Math.min(lines, MAX_LINES);
    }

    private List<String> readTail(Path path, int lines) {
        Deque<String> buffer = new ArrayDeque<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (buffer.size() == lines) {
                    buffer.pollFirst();
                }
                buffer.addLast(line);
            }
        } catch (IOException e) {
            throw new ManagementException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
        }
        return List.copyOf(buffer);
    }
}
