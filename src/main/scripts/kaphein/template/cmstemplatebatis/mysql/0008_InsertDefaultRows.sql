INSERT INTO t_app_user
(
    user_type,
    login_name,
    login_password,
    real_name,
    email_address
)
VALUES
(
    1,
    'root',
    '$2a$12$5nDqH8qcSq.JdgM3CJll0...O7ARN34UDuSd7mTDX3qWl.d8fhzse',     -- 1234
    'Foo Bar',
    'root@abcxyz.com'
)
;

INSERT INTO t_role_permission_entry
(
    user_uid,
    permissionUid
)
VALUES
(
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'dashboard.read')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'dashboard.write')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'user.read')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'user.write')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'permission.read')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'permission.write')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'action.read')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'action.write')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'menu.read')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'menu.write')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'notice.read')
)
, (
    (SELECT userUid FROM t_app_user WHERE loginName = 'root')
    , (SELECT permissionUid FROM listPermission WHERE permissionName = 'notice.write')
)
;
