# 用户信息表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
(
    user_id     bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
    username    varchar(30)         NOT NULL DEFAULT '' COMMENT '用户账号',
    nickname    varchar(30)         NOT NULL DEFAULT '' COMMENT '用户昵称',
    gender      tinyint(5)          NOT NULL DEFAULT 0 COMMENT '性别,0:未知,1:男,2:女',
    avatar      varchar(256)        NOT NULL DEFAULT '' COMMENT '头像地址',
    email       varchar(50)         NOT NULL DEFAULT '' COMMENT '用户邮箱',
    mobile      varchar(20)         NOT NULL DEFAULT '' COMMENT '手机号码',
    user_pwd    varchar(64)         NOT NULL DEFAULT '' COMMENT '登录口令密文',
    login_ip    varchar(128)        NOT NULL DEFAULT '' COMMENT '最后登录IP',
    login_time  datetime                     DEFAULT NULL COMMENT '最后登录时间',
    status      tinyint(5)          NOT NULL DEFAULT 0 COMMENT '帐号状态,0:正常,1:停用',
    deleted     tinyint(5)          NOT NULL DEFAULT 0 COMMENT '删除标志,0:未删除,1:已删除',
    create_by   varchar(64)         NOT NULL DEFAULT '' COMMENT '创建者',
    update_by   varchar(64)         NOT NULL DEFAULT '' COMMENT '更新者',
    create_time datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      varchar(500)        NOT NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (user_id),
    UNIQUE KEY uniq_user_name (username)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10000 COMMENT = '用户信息表';

-- 初始化-用户信息表数据  --------
INSERT INTO sys_user (user_id, username, nickname, gender, avatar, email, mobile, user_pwd, login_ip, login_time,
                      remark)
VALUES (1001, 'admin', '管理员', 1, '', 'moyu@126.com', '15888888888', 'pwd', '127.0.0.1', SYSDATE(), '管理员'),
       (1002, 'moyu', '魔芋', 2, '', 'moyu@126.com', '15888888888', 'pwd', '127.0.0.1', SYSDATE(), '管理员');
-- ----------------------------

# 角色信息表
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role
(
    id          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    role_name   varchar(30)         NOT NULL COMMENT '角色名称',
    role_key    varchar(100)        NOT NULL COMMENT '角色权限字符串',
    sort_order  int(5)              NOT NULL COMMENT '显示顺序',
    status      tinyint(5)          NOT NULL DEFAULT 0 COMMENT '角色状态,0:正常,1:停用',
    deleted     tinyint(5)          NOT NULL DEFAULT 0 COMMENT '删除标志,0:未删除,1:已删除',
    create_by   varchar(64)         NOT NULL DEFAULT '' COMMENT '创建者',
    update_by   varchar(64)         NOT NULL DEFAULT '' COMMENT '更新者',
    create_time datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      varchar(500)        NOT NULL DEFAULT '' COMMENT '备注',
    primary key (id),
    UNIQUE KEY uniq_role_key (role_key)
) ENGINE = innodb
  AUTO_INCREMENT = 100 COMMENT = '角色信息表';

-- 初始化-角色信息表数据  --------
INSERT INTO sys_role (id, role_name, role_key, sort_order, remark)
VALUES (101, '管理员', 'admin', 1, '管理员'),
       (102, '普通职员', 'normal', 2, '普通角色');
-- ----------------------------

# 菜单权限表
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu
(
    id          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    menu_name   varchar(50)         NOT NULL COMMENT '菜单名称',
    parent_id   bigint(20)          NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    sort_order  int(5)              NOT NULL DEFAULT 0 COMMENT '显示顺序',
    menu_type   char(1)             NOT NULL DEFAULT '' COMMENT '资源类型,D:目录,M:菜单,B:按钮',
    path        varchar(256)        NOT NULL DEFAULT '' COMMENT '路径地址',
    component   varchar(256)        NOT NULL DEFAULT '' COMMENT '组件路径',
    perms       varchar(100)        NOT NULL DEFAULT '' COMMENT '权限标识',
    icon        varchar(100)        NOT NULL DEFAULT '#' COMMENT '菜单图标',
    hidden      tinyint(5)          NOT NULL DEFAULT 0 COMMENT '是否隐藏,0:显示,1:隐藏',
    link        tinyint(5)          NOT NULL DEFAULT 0 COMMENT '是否外链,0:非外链,1:外链',
    status      tinyint(5)          NOT NULL DEFAULT 0 COMMENT '菜单状态,0:正常,1:停用',
    deleted     tinyint(5)          NOT NULL DEFAULT 0 COMMENT '删除标志,0:未删除,1:已删除',
    create_by   varchar(64)         NOT NULL DEFAULT '' COMMENT '创建者',
    update_by   varchar(64)         NOT NULL DEFAULT '' COMMENT '更新者',
    create_time datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      varchar(500)        NOT NULL DEFAULT '' COMMENT '备注',
    primary key (id)
) ENGINE = innodb
  AUTO_INCREMENT = 2000 COMMENT = '菜单权限表';


-- 初始化-菜单信息表数据 ---------
-- 一级菜单
INSERT INTO sys_menu (id, menu_name, parent_id, sort_order, menu_type, path, component, perms, icon, hidden, link,
                      remark)
VALUES (1, '系统管理', 0, 1, 'D', 'system', '', '', 'system', 0, 0, '系统管理目录'),
       (2, '系统监控', 0, 2, 'D', 'monitor', '', '', 'monitor', 0, 0, '系统监控目录'),
       (3, '系统工具', 0, 3, 'D', 'tool', '', '', 'tool', 0, 0, '系统工具目录'),
       (4, '魔芋官网', 0, 4, 'D', 'https://moyu.dodoyd.com', '', '', 'guide', 0, 0, '魔芋官网地址');

-- 二级菜单
INSERT INTO sys_menu (id, menu_name, parent_id, sort_order, menu_type, path, component, perms, icon, hidden, link,
                      remark)
VALUES (101, '用户管理', 1, 1, 'M', 'user', 'system/user/index', 'system:user:list', 'user', 0, 0, '系统管理菜单'),
       (102, '角色管理', 1, 2, 'M', 'role', 'system/role/index', 'system:role:list', 'peoples', 0, 0, '角色管理菜单'),
       (103, '菜单管理', 1, 3, 'M', 'menu', 'system/menu/index', 'system:menu:list', 'tree-table', 0, 0,
        '菜单管理菜单'),
       (104, '代码生成', 3, 1, 'M', 'gen', 'tool/gen/index', 'system:gen:list', 'code', 0, 0, '代码生成菜单');

-- 用户管理按钮
INSERT INTO sys_menu (id, menu_name, parent_id, sort_order, menu_type, path, component, perms, icon)
VALUES (1001, '用户查询', 101, 1, 'B', '', '', 'system:user:query', '#'),
       (1002, '用户新增', 101, 2, 'B', '', '', 'system:user:add', '#'),
       (1003, '用户修改', 101, 3, 'B', '', '', 'system:user:edit', '#'),
       (1004, '用户删除', 101, 4, 'B', '', '', 'system:user:remove', '#'),
       (1005, '重置密码', 101, 5, 'B', '', '', 'system:user:resetPwd', '#');
-- 角色管理按钮
INSERT INTO sys_menu (id, menu_name, parent_id, sort_order, menu_type, path, component, perms, icon)
VALUES (1006, '角色查询', 102, 1, 'B', '', '', 'system:role:query', '#'),
       (1007, '角色新增', 102, 2, 'B', '', '', 'system:role:add', '#'),
       (1008, '角色修改', 102, 3, 'B', '', '', 'system:role:edit', '#'),
       (1009, '角色删除', 102, 4, 'B', '', '', 'system:role:remove', '#');
-- 菜单管理按钮
INSERT INTO sys_menu (id, menu_name, parent_id, sort_order, menu_type, path, component, perms, icon)
VALUES (1010, '菜单查询', 103, 1, 'B', '', '', 'system:menu:query', '#'),
       (1011, '菜单新增', 103, 2, 'B', '', '', 'system:menu:add', '#'),
       (1012, '菜单修改', 103, 3, 'B', '', '', 'system:menu:edit', '#'),
       (1013, '菜单删除', 103, 4, 'B', '', '', 'system:menu:remove', '#');
-- 代码生成按钮
INSERT INTO sys_menu (id, menu_name, parent_id, sort_order, menu_type, path, component, perms, icon)
VALUES (1014, '生成查询', 104, 1, 'B', '', '', 'system:gen:query', '#'),
       (1015, '导入代码', 104, 2, 'B', '', '', 'system:gen:import', '#'),
       (1016, '预览代码', 104, 3, 'B', '', '', 'system:gen:preview', '#'),
       (1017, '生成代码', 104, 4, 'B', '', '', 'system:gen:code', '#');
-- ----------------------------

# 用户和角色关联表  用户N-1角色
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role
(
    id      bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    user_id bigint(20)          not null comment '用户ID',
    role_id bigint(20)          not null comment '角色ID',
    PRIMARY KEY (id),
    UNIQUE KEY uniq_user_role (user_id, role_id)
) ENGINE = innodb comment = '用户和角色关联表';

-- 初始化-用户和角色关联表数据 ----
INSERT INTO sys_user_role (user_id, role_id)
VALUES (1001, 101),
       (1002, 101);
-- ----------------------------

# 角色和菜单关联表  角色1-N菜单
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu
(
    id      bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    role_id bigint(20)          NOT NULL COMMENT '角色ID',
    menu_id bigint(20)          NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (id),
    UNIQUE KEY uniq_role_menu (role_id, menu_id)
) ENGINE = innodb COMMENT = '角色和菜单关联表';

-- 初始化-用户和角色关联表数据 ----
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (101, 1),
       (101, 2),
       (101, 3),
       (101, 4),
       (101, 101),
       (101, 102),
       (101, 103),
       (101, 104),
       (101, 1001),
       (101, 1002),
       (101, 1003),
       (101, 1004),
       (101, 1005),
       (101, 1006),
       (101, 1007),
       (101, 1008),
       (101, 1009),
       (101, 1010),
       (101, 1011),
       (101, 1012),
       (101, 1013),
       (101, 1014);
-- ----------------------------
