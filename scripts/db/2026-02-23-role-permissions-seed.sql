-- 기본 권한/역할 시드 (선택 실행)

INSERT INTO app_permissions (code, description)
VALUES
    ('PERM_ADMIN_UI', '관리자 UI 접근'),
    ('PERM_STATUS_READ', '상태 API 조회'),
    ('PERM_CONFIG_READ', 'Config 조회'),
    ('PERM_CONFIG_WRITE', 'Config 변경'),
    ('PERM_OUTBOUND_READ', 'Outbound 조회'),
    ('PERM_OUTBOUND_WRITE', 'Outbound 제어'),
    ('PERM_STAT_READ', 'Stat 조회'),
    ('PERM_TSERVER_WRITE', 'T-Server 제어');

INSERT INTO app_roles (name, description, enabled)
VALUES
    ('ADMIN', '관리자', 1),
    ('OPERATOR', '운영자', 1);

-- ADMIN 역할에 모든 권한 매핑
INSERT INTO app_role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM app_roles r
CROSS JOIN app_permissions p
WHERE r.name = 'ADMIN';

-- OPERATOR 역할에 조회 권한만 매핑
INSERT INTO app_role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM app_roles r
JOIN app_permissions p ON p.code IN ('PERM_STATUS_READ', 'PERM_CONFIG_READ', 'PERM_OUTBOUND_READ', 'PERM_STAT_READ')
WHERE r.name = 'OPERATOR';
