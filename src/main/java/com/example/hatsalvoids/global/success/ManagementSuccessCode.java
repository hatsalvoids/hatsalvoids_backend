package com.example.hatsalvoids.global.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ManagementSuccessCode implements SuccessCode {
    SERVER_STATUS_FETCHED(HttpStatus.OK, "서버 자원 상태를 조회했습니다."),
    ERROR_LOG_FETCHED(HttpStatus.OK, "에러 로그를 조회했습니다."),
    APP_LOG_FETCHED(HttpStatus.OK, "평시 로그를 조회했습니다.");

    private final HttpStatus status;
    private final String message;
}
