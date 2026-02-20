# 서버 관리 가이드

## 개요
이 문서는 프로메테우스 및 Actuator 기반 서버 분석과 보안 관리 정책을 설명합니다.
관리 API는 prod 환경에서 Authorization 헤더(role == "ADMIN")가 필요합니다.

## 보안 정책
- prod 환경에서만 관리 API 및 Actuator에 대한 권한 체크가 적용됩니다.
- Authorization 헤더의 토큰 claim 중 role == "ADMIN"일 때만 접근 가능합니다.

## 관리 API 목록

### 1) 서버 자원 status 조회 API
GET `/api/v1/management/status`

요청 예시:
```
curl -H "Authorization: Bearer {token}" \
  "https://{host}/api/v1/management/status"
```

응답 예시:
```
{
  "status": 200,
  "message": "서버 자원 상태를 조회했습니다.",
  "result": {
    "timestamp": "2026-01-31T10:30:12.345+09:00",
    "systemCpuUsage": 0.12,
    "processCpuUsage": 0.04,
    "heapUsedBytes": 123456789,
    "heapMaxBytes": 987654321,
    "threadCount": 42,
    "uptimeMillis": 3600000
  }
}
```

### 2) 서버 에러 로그 조회 API
GET `/api/v1/management/logs/error?lines=200`

요청 예시:
```
curl -H "Authorization: Bearer {token}" \
  "https://{host}/api/v1/management/logs/error?lines=200"
```

응답 예시:
```
{
  "status": 200,
  "message": "에러 로그를 조회했습니다.",
  "result": {
    "filePath": "/app/logs/error.log",
    "lines": 200,
    "content": [
      "2026-01-31 10:29:58.120 ERROR [http-nio-8080-exec-1] ...",
      "2026-01-31 10:29:59.001 ERROR [http-nio-8080-exec-2] ..."
    ]
  }
}
```

### 3) 평시 로그 파일 읽기 API
GET `/api/v1/management/logs/app?lines=200`

요청 예시:
```
curl -H "Authorization: Bearer {token}" \
  "https://{host}/api/v1/management/logs/app?lines=200"
```

응답 예시:
```
{
  "status": 200,
  "message": "평시 로그를 조회했습니다.",
  "result": {
    "filePath": "/app/logs/app.log",
    "lines": 200,
    "content": [
      "2026-01-31 10:29:00.100 INFO  [http-nio-8080-exec-1] ...",
      "2026-01-31 10:29:01.250 INFO  [http-nio-8080-exec-2] ..."
    ]
  }
}
```

## Actuator 및 Prometheus
- Actuator base path: `/actuator`
- 노출 엔드포인트: `health`, `info`, `metrics`, `prometheus`

Prometheus 스크레이프 예시:
```
curl -H "Authorization: Bearer {token}" \
  "https://{host}/actuator/prometheus"
```

## 그늘 정보 요청 레이트 리밋
- 동일 사용자 기준 10초 이내 3회 초과 요청 시 `429 Too Many Requests` 반환
- 기준 식별자는 토큰 claim(userId, email, sub) 또는 IP를 사용합니다.
