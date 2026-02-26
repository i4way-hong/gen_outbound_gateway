-- 신규 로그인 시 기존 토큰 무효화를 위한 토큰 버전 컬럼 추가
-- 대상: app_users

ALTER TABLE app_users
    ADD token_version BIGINT NOT NULL DEFAULT 0;

-- 기존 레코드 기본값 보장
UPDATE app_users
SET token_version = 0
WHERE token_version IS NULL;
