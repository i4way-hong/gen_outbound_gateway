# CCC 요청/응답 샘플 (2026-02-04)

## 공통 규칙
- CCC 요청/응답은 `encData` 필드를 사용합니다.
- 서버는 `app.security.encryption.enabled=true`일 때 자동으로 복호화/암호화를 수행합니다.

## 스킬그룹 상담사 상태 조회
### 평문 요청
```json
{
  "NAME": "SalesSkillGroup"
}
```

### 평문 응답
```json
{
  "success": true,
  "message": "스킬그룹 상담사 상태 조회",
  "data": {
    "USERS": [
      { "EMPLOYEE_ID": "10001", "STAT": "Ready" },
      { "EMPLOYEE_ID": "10002", "STAT": "NotReady" }
    ]
  },
  "timestamp": "2026-02-04T16:27:05+09:00"
}
```

## 상담사 로그아웃
### 평문 요청
```json
{
  "INLINE_NUM": "3001"
}
```

### 평문 응답
```json
{
  "success": true,
  "message": "상담사 로그아웃",
  "data": {
    "RESULT": "T",
    "LOGIN_ID": "10001",
    "RESULT_MSG": ""
  },
  "timestamp": "2026-02-04T16:27:05+09:00"
}
```

## 상담사 대기/이석
### 평문 요청 (대기)
```json
{
  "INLINE_NUM": "3001"
}
```

### 평문 요청 (이석)
```json
{
  "INLINE_NUM": "3001",
  "REASON_CODE": "10"
}
```

### 평문 응답
```json
{
  "success": true,
  "message": "상담사 대기",
  "data": {
    "RESULT": "T",
    "LOGIN_ID": "10001",
    "RESULT_MSG": ""
  },
  "timestamp": "2026-02-04T16:27:05+09:00"
}
```

## 상담사 상태 확인
### 평문 요청
```json
{
  "INLINE_NUM": "3001"
}
```

### 평문 응답
```json
{
  "success": true,
  "message": "상담사 상태 확인",
  "data": {
    "RESULT": "T",
    "LOGIN_ID": "10001",
    "RESULT_MSG": ""
  },
  "timestamp": "2026-02-04T16:27:05+09:00"
}
```

## 암호화 요청/응답 형식
### 암호화 요청 예시
```json
{
  "encData": "BASE64_AES_ENCRYPTED_PAYLOAD"
}
```

### 암호화 응답 예시
```json
{
  "encData": "BASE64_AES_ENCRYPTED_RESPONSE"
}
```
