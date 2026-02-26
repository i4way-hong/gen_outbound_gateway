-- 역할/권한 테이블 생성 및 기본 인덱스 (수동 실행용)
-- 대상: MS SQL Server (필요 시 타입/제약 조정)

CREATE TABLE app_roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(255) NULL,
    enabled BIT NOT NULL DEFAULT 1
);

CREATE TABLE app_permissions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(150) NOT NULL UNIQUE,
    description NVARCHAR(255) NULL
);

CREATE TABLE app_role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    CONSTRAINT pk_app_role_permissions PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES app_roles(id),
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES app_permissions(id)
);

CREATE TABLE app_user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_app_user_roles PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES app_users(id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES app_roles(id)
);

CREATE INDEX idx_app_roles_enabled ON app_roles(enabled);
CREATE INDEX idx_app_permissions_code ON app_permissions(code);
CREATE INDEX idx_app_role_permissions_role ON app_role_permissions(role_id);
CREATE INDEX idx_app_role_permissions_permission ON app_role_permissions(permission_id);
CREATE INDEX idx_app_user_roles_user ON app_user_roles(user_id);
CREATE INDEX idx_app_user_roles_role ON app_user_roles(role_id);
