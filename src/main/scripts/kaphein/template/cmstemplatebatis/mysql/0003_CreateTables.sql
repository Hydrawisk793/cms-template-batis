-- ----------------------------------------------------------------
-- 사용자

CREATE TABLE IF NOT EXISTS t_app_user
(
    -- 사용자 UID
    user_uid                        BIGINT NOT NULL,

    -- 사용자 타입
    user_type                       INTEGER NOT NULL,

    -- 로그인 ID
    login_name                      VARCHAR(256),

    -- 로그인 비밀번호
    login_password                  VARCHAR(1024),

    -- 실명
    real_name                       VARCHAR(128),

    -- 이메일 주소
    email_address                   VARCHAR(2048),

    -- 생성 일시
    created_at                      DATETIME NOT NULL,

    -- 생성한 관리자의 UID
    created_by                      BIGINT,

    -- 수정 일시
    updated_at                      DATETIME,

    -- 수정한 관리자의 UID
    updated_by                      BIGINT,

    -- 삭제 일시
    deleted_at                      DATETIME,

    -- 삭제한 관리자의 UID
    deleted_by                      BIGINT
);

ALTER TABLE t_app_user
    ADD CONSTRAINT pk__t_app_user PRIMARY KEY (user_uid);

ALTER TABLE t_app_user
    MODIFY COLUMN user_uid BIGINT NOT NULL AUTO_INCREMENT;

-- ----------------------------------------------------------------

-- ----------------------------------------------------------------
-- 로그인 로그

CREATE TABLE IF NOT EXISTS t_login_log
(
    -- 로그인 로그 UID
    login_log_uid                   BIGINT NOT NULL,

    -- 사용자 UID
    user_uid                        BIGINT NOT NULL,

    -- 로그인 당시 로그인 ID
    login_name                      VARCHAR(256) NOT NULL,

    -- IP 주소
    ip_address                      VARCHAR(1024) NOT NULL,

    -- 로그인 성공여부
    -- 0 : 실패, 0 이외 : 성공
    login_succeeded                 INT NOT NULL,

    -- 생성 일시
    created_at                      DATETIME NOT NULL
);

ALTER TABLE t_login_log
    ADD CONSTRAINT pk__t_login_log PRIMARY KEY (login_log_uid);

ALTER TABLE t_login_log
    MODIFY COLUMN login_log_uid BIGINT NOT NULL AUTO_INCREMENT;

-- ----------------------------------------------------------------

-- ----------------------------------------------------------------
-- Remember me 토큰

CREATE TABLE IF NOT EXISTS t_remember_me_token
(
    -- 토큰 ID
    series_id                       VARCHAR(256) NOT NULL,

    -- 로그인 ID
    login_name                      VARCHAR(256) NOT NULL,

    -- 토큰 값
    token_value                     VARCHAR(256) NOT NULL,

    -- 마지막 사용 일시
    last_used_at                    DATETIME NOT NULL
);

ALTER TABLE t_remember_me_token
    ADD CONSTRAINT pk__t_remember_me_token PRIMARY KEY (series_id);

-- ----------------------------------------------------------------

-- ----------------------------------------------------------------
-- 사용자 권한

CREATE TABLE IF NOT EXISTS t_permission
(
    -- 권한 이름
    -- 패턴 : ^[0-9a-zA-Z_](1,256)$
    permission_name                 VARCHAR(256) NOT NULL,

    -- 표시 이름
    display_name                    VARCHAR(1024),

    -- 생성 일시
    created_at                      DATETIME NOT NULL,

    -- 생성한 관리자 UID
    created_by                      BIGINT,

    -- 수정 일시
    updated_at                      DATETIME,

    -- 수정한 관리자 UID
    updated_by                      BIGINT
);

ALTER TABLE t_permission
    ADD CONSTRAINT pk__t_permission PRIMARY KEY (permission_name);

-- ----------------------------------------------------------------

-- ----------------------------------------------------------------
-- 사용자 역할

CREATE TABLE IF NOT EXISTS t_role
(
    -- 역할 이름
    -- 패턴 : ^[0-9a-zA-Z_](1,256)$
    role_name                       VARCHAR(256) NOT NULL,

    -- 표시 이름
    display_name                    VARCHAR(1024),

    -- 생성 일시
    created_at                      DATETIME NOT NULL,

    -- 생성한 관리자 UID
    created_by                      BIGINT,

    -- 수정 일시
    updated_at                      DATETIME,

    -- 수정한 관리자 UID
    updated_by                      BIGINT
);

ALTER TABLE t_role
    ADD CONSTRAINT pk__t_role PRIMARY KEY (role_name);

-- ----------------------------------------------------------------

-- ----------------------------------------------------------------
-- 역할 <- 권한 엔트리

CREATE TABLE IF NOT EXISTS t_role_permission_entry
(
    -- 엔트리 UID
    entry_uid                       BIGINT NOT NULL,

    -- 역할 이름
    role_name                       VARCHAR(256) NOT NULL,

    -- 권한 이름
    permission_name                 VARCHAR(256) NOT NULL,

    -- 생성 일시
    created_at                      DATETIME NOT NULL,

    -- 생성한 관리자 UID
    created_by                      BIGINT
);

ALTER TABLE t_role_permission_entry
    ADD CONSTRAINT pk__t_role_permission_entry PRIMARY KEY (entry_uid);

ALTER TABLE t_role_permission_entry
    ADD CONSTRAINT uq__t_role_permission_entry__rn_pn UNIQUE (role_name, permission_name);

-- ----------------------------------------------------------------

-- ----------------------------------------------------------------
-- 사용자 <- 역할 엔트리

CREATE TABLE IF NOT EXISTS t_user_role_entry
(
    -- 엔트리 UID
    entry_uid                       BIGINT NOT NULL,

    -- 사용자 UID
    user_uid                        BIGINT NOT NULL,

    -- 역할 이름
    role_name                       VARCHAR(256) NOT NULL,

    -- 생성 일시
    created_at                      DATETIME NOT NULL,

    -- 생성한 관리자 UID
    created_by                      BIGINT
);

ALTER TABLE t_user_role_entry
    ADD CONSTRAINT pk__t_user_role_entry PRIMARY KEY (entry_uid);

ALTER TABLE t_user_role_entry
    ADD CONSTRAINT uq__t_user_role_entry__uu_rn UNIQUE (user_uid, role_name);

-- ----------------------------------------------------------------

-- ----------------------------------------------------------------
-- 액션

CREATE TABLE IF NOT EXISTS t_action
(
    -- 액션 이름
    -- 패턴 : ^[0-9a-zA-Z_](1,256)$
    action_name                     VARCHAR(256) NOT NULL,

    -- 액션 종류
    -- 0 : 없음, 1 : URL
    action_type                     VARCHAR(256) NOT NULL,

    -- 액션 설명
    action_description              TEXT,

    -- 액션 URL
    -- `action_type`이 1일 때만 유효
    action_url                      VARCHAR(4096),

    -- 생성 일시
    created_at                      DATETIME NOT NULL,

    -- 생성한 관리자 UID
    created_by                      BIGINT,

    -- 수정 일시
    updated_at                      DATETIME,

    -- 수정한 관리자 UID
    updated_by                      BIGINT
);

ALTER TABLE t_action
    ADD CONSTRAINT pk__t_action PRIMARY KEY (action_name);

-- ----------------------------------------------------------------

-- ----------------------------------------------------------------
-- 액션 <- 권한 엔트리

CREATE TABLE IF NOT EXISTS t_action_permission_entry
(
    -- 액션 이름
    action_name                     VARCHAR(256) NOT NULL,

    -- 권한 이름
    permission_name                 VARCHAR(256) NOT NULL,

    -- 생성 일시
    created_at                      DATETIME NOT NULL,

    -- 생성한 관리자 UID
    created_by                      BIGINT
);

ALTER TABLE t_action_permission_entry
    ADD CONSTRAINT pk__t_action_permission_entry PRIMARY KEY (action_name, permission_name);

-- ----------------------------------------------------------------
