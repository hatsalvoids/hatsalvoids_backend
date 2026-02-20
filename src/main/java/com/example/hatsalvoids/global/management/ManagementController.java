package com.example.hatsalvoids.global.management;

import com.example.hatsalvoids.global.management.model.LogFileResponse;
import com.example.hatsalvoids.global.management.model.ServerStatusResponse;
import com.example.hatsalvoids.global.success.ManagementSuccessCode;
import com.example.hatsalvoids.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/management")
public class ManagementController {

    private final ManagementService managementService;
    private final LogFileService logFileService;

    @GetMapping("/status")
    public ResponseEntity<SuccessResponse<ServerStatusResponse>> getServerStatus() {
        ServerStatusResponse response = managementService.getServerStatus();
        return ResponseEntity
                .status(ManagementSuccessCode.SERVER_STATUS_FETCHED.getStatus())
                .body(SuccessResponse.of(ManagementSuccessCode.SERVER_STATUS_FETCHED, response));
    }

    @GetMapping("/logs/error")
    public ResponseEntity<SuccessResponse<LogFileResponse>> getErrorLog(
            @RequestParam(required = false) Integer lines
    ) {
        LogFileResponse response = logFileService.readErrorLog(lines);
        return ResponseEntity
                .status(ManagementSuccessCode.ERROR_LOG_FETCHED.getStatus())
                .body(SuccessResponse.of(ManagementSuccessCode.ERROR_LOG_FETCHED, response));
    }

    @GetMapping("/logs/app")
    public ResponseEntity<SuccessResponse<LogFileResponse>> getAppLog(
            @RequestParam(required = false) Integer lines
    ) {
        LogFileResponse response = logFileService.readAppLog(lines);
        return ResponseEntity
                .status(ManagementSuccessCode.APP_LOG_FETCHED.getStatus())
                .body(SuccessResponse.of(ManagementSuccessCode.APP_LOG_FETCHED, response));
    }
}
