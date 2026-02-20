package com.example.hatsalvoids.shade.common;

import com.example.hatsalvoids.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ShadeErrorCode implements ErrorCode {
    SOLAR_IS_UNDER(HttpStatus.BAD_REQUEST, "지금 시간대에는 그늘이 없어요. 입력 시각 : %s, 그늘 조회 가능 시간 : %s 까지"),
    SHADE_COMMON_ERROR(HttpStatus.BAD_REQUEST, "그늘 처리 중 예외가 발생했습니다. 내용 : %s"),
    POINT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "유효한 LinearRing 좌표를 찾지 못했습니다."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "요청 횟수를 초과했습니다. 10초 이내 최대 3회까지 허용됩니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() { return status; }
    @Override
    public String getMessage() { return message; }
} 