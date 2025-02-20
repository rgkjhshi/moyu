-- 菜单数据
insert into moyu.sys_menu (id, parent_code, name, code, menu_type, path, component, icon, permission, visible, link, module, sort_num, status, delete_flag, ext_json, remark, create_time, create_user, update_time, update_user)
values  (2001, '0', '系统模块', 'sys_module', 1, '/sysModule', '', 'appstore-add-outlined', '', 1, null, null, 1, 0, 0, null, '', null, null, '2025-02-19 22:22:35', null),
        (2003, 'sys_module', '组织架构', '1548901111999770726', 2, '/org', 'Layout', 'apartment-outlined', '', 1, null, 'sys_module', 4, 0, 0, null, '', null, null, '2025-01-28 01:26:56', null),
        (2004, '1548901111999770726', '组织管理', '1548901111999770826', 3, '/sys/org', 'sys/org/index', 'cluster-outlined', null, 1, null, 'sys_module', 5, 0, 0, null, '', null, null, null, null),
        (2005, '1548901111999770726', '用户管理', '1548901111999770926', 3, '/sys/user', 'sys/user/index', 'user-outlined', '', 1, null, 'sys_module', 6, 0, 0, null, '', null, null, '2025-01-17 15:14:44', null),
        (2006, '1548901111999770726', '分组管理', '1548901111999771026', 3, '/sys/group', 'sys/group/index', 'apartment-outlined', '', 1, null, 'sys_module', 7, 0, 0, null, '', null, null, '2025-01-20 13:33:24', null),
        (2007, 'sys_module', '权限管控', '1548901111999771126', 2, '/perm', 'Layout', 'user-switch-outlined', '', 1, null, 'sys_module', 8, 0, 0, null, '', null, null, '2025-02-07 10:23:24', null),
        (2008, '1548901111999771126', '角色管理', '1548901111999771226', 3, '/sys/role', 'sys/role/index', 'deployment-unit-outlined', '', 1, null, 'sys_module', 9, 0, 0, null, '', null, null, '2025-02-06 17:38:38', null),
        (2009, '1548901111999771126', '模块管理', '1548901111999771326', 3, '/sys/module', 'sys/resource/module/index', 'appstore-add-outlined', '', 1, null, 'sys_module', 10, 0, 0, null, '', null, null, '2025-02-06 17:38:33', null),
        (2010, '1548901111999771126', '菜单管理', '1548901111999771426', 3, '/sys/menu', 'sys/resource/menu/index', 'pic-left-outlined', '', 1, null, 'sys_module', 11, 0, 0, null, '', null, null, '2025-02-06 17:38:27', null),
        (2011, 'sys_module', '基础工具', '1548901111999771626', 2, '/tool', 'Layout', 'tool-outlined', '', 1, null, 'sys_module', 13, 0, 0, null, '', null, null, '2025-01-28 01:26:33', null),
        (2012, '1548901111999771626', '文件管理', '1548901111999771726', 3, '/dev/file/index', 'dev/file/index', 'copy-outlined', null, 1, null, 'sys_module', 14, 0, 0, null, '', null, null, null, null),
        (2013, '1548901111999771626', '邮件推送', '1548901111999771826', 3, '/dev/email/index', 'dev/email/index', 'send-outlined', null, 1, null, 'sys_module', 15, 0, 0, null, '', null, null, null, null),
        (2014, '1548901111999771626', '短信发送', '1548901111999771926', 3, '/dev/sms/index', 'dev/sms/index', 'mail-outlined', null, 1, null, 'sys_module', 16, 0, 0, null, '', null, null, null, null),
        (2015, '1548901111999771626', '站内信息', '1548901111999772026', 3, '/dev/message/index', 'dev/message/index', 'message-outlined', null, 1, null, 'sys_module', 17, 0, 0, null, '', null, null, null, null),
        (2016, 'sys_module', '系统运维', '1548901111999772126', 2, '/ops', 'Layout', 'hdd-outlined', '', 1, null, 'sys_module', 18, 0, 0, null, '', null, null, '2025-01-28 01:26:12', null),
        (2017, '1548901111999772126', '三方用户', '1548901111999772226', 3, '/auth/third', 'auth/third/index', 'team-outlined', null, 1, null, 'sys_module', 19, 0, 0, null, '', null, null, null, null),
        (2018, '1548901111999772126', '数据字典', '1548901111999772326', 3, '/sys/dict', 'dev/dict/index', 'file-search-outlined', null, 1, null, 'sys_module', 20, 0, 0, null, '', null, null, null, null),
        (2019, '1548901111999772126', '系统配置', '1548901111999772426', 3, '/sys/config', 'dev/config/index', 'setting-outlined', null, 1, null, 'sys_module', 21, 0, 0, null, '', null, null, null, null),
        (2020, '1548901111999772126', '任务调度', '1548901111999772526', 3, '/dev/job', 'dev/job/index', 'field-time-outlined', null, 1, null, 'sys_module', 22, 0, 0, null, '', null, null, null, null),
        (2021, '1548901111999772126', '会话管理', '1548901111999772626', 3, '/auth/monitor', 'auth/monitor/index', 'usergroup-delete-outlined', null, 1, null, 'sys_module', 23, 0, 0, null, '', null, null, null, null),
        (2022, '1548901111999772126', '系统监控', '1548901111999772726', 3, '/dev/monitor', 'dev/monitor/index', 'database-outlined', null, 1, null, 'sys_module', 24, 0, 0, null, '', null, null, null, null),
        (2023, '1548901111999772126', '连接监控', '1548901111999772826', 5, 'http://localhost:82/druid/index.html', '', 'console-sql-outlined', '', 1, null, 'sys_module', 25, 0, 0, null, '', null, null, '2025-01-30 02:12:26', null),
        (2024, '1548901111999772126', '接口文档', '1548901111999772926', 5, 'http://localhost:82/doc.html', '', 'file-word-outlined', '', 1, null, 'sys_module', 29, 0, 0, null, '', null, null, '2025-01-30 02:12:14', null),
        (2025, '1548901111999772126', '日志审计', '1548901111999773126', 2, '/x1vjuegii4', null, 'robot-outlined', null, 1, null, 'sys_module', 27, 0, 0, null, '', null, null, null, null),
        (2026, '1548901111999773126', '访问日志', '1548901111999773226', 3, '/dev/vislog', 'dev/log/vislog/index', 'bars-outlined', null, 1, null, 'sys_module', 28, 0, 0, null, '', null, null, null, null),
        (2027, 'sys_module', '在线开发', '1548901111999773250', 2, '/dev', 'Layout', 'project-outlined', '', 1, null, 'sys_module', 41, 0, 0, null, '', null, null, '2025-01-28 01:25:53', null),
        (2028, '1548901111999773250', '代码生成', '1548901111999773254', 3, '/dev/gen', 'gen/index', 'rocket-outlined', '', 1, null, 'sys_module', 45, 0, 0, null, '', null, null, '2025-02-19 22:51:28', null),
        (2029, '1548901111999773126', '操作日志', '1548901111999773326', 3, '/dev/oplog', 'dev/log/oplog/index', 'bars-outlined', null, 1, null, 'sys_module', 29, 0, 0, null, '', null, null, null, null),
        (2050, '0', '业务模块', 'biz_module', 1, '/bizModule', '', 'profile-outlined', '', 1, null, null, 2, 0, 0, null, '', null, null, '2025-02-19 22:22:42', null),
        (2051, 'biz_module', '公司架构', '1548901111999773977', 2, '/1nlpdpnief', 'Layout', 'cluster-outlined', '', 1, null, 'biz_module', 51, 0, 0, null, '', null, null, '2025-02-19 22:48:10', null),
        (2052, '1548901111999773977', '机构管理', '1548901111999773978', 3, '/biz/org', 'biz/org/index', 'cluster-outlined', null, 1, null, 'biz_module', 52, 0, 0, null, '', null, null, null, null),
        (2053, '1548901111999773977', '人员管理', '1548901111999773979', 3, '/biz/user', 'biz/user/index', 'user-outlined', null, 1, null, 'biz_module', 53, 0, 0, null, '', null, null, null, null),
        (2054, '1548901111999773977', '岗位管理', '1548901111999773980', 3, '/biz/position', 'biz/position/index', 'apartment-outlined', null, 1, null, 'biz_module', 54, 0, 0, null, '', null, null, null, null),
        (2055, '1548901111999773978', '新增机构', '1571129529564758017', 4, null, null, null, null, 1, null, 'biz_module', 1, 0, 0, null, '', null, null, null, null),
        (2056, '1548901111999773978', '批量删除', '1571129929961406466', 4, null, null, null, null, 1, null, 'biz_module', 2, 0, 0, null, '', null, null, null, null),
        (2057, '1548901111999773978', '编辑机构', '1571130756155408386', 4, null, null, null, null, 1, null, 'biz_module', 3, 0, 0, null, '', null, null, null, null),
        (2058, '1548901111999773978', '删除机构', '1571130811058847745', 4, null, null, null, null, 1, null, 'biz_module', 4, 0, 0, null, '', null, null, null, null),
        (2059, '1548901111999773979', '新增人员', '1571130973294526465', 4, null, null, null, null, 1, null, 'biz_module', 1, 0, 0, null, '', null, null, null, null),
        (2060, '1548901111999773979', '批量删除', '1571131043532341249', 4, null, null, null, null, 1, null, 'biz_module', 2, 0, 0, null, '', null, null, null, null),
        (2061, '1548901111999773979', '编辑人员', '1571131137006600193', 4, null, null, null, null, 1, null, 'biz_module', 3, 0, 0, null, '', null, null, null, null),
        (2062, '1548901111999773979', '授权角色', '1571131427361488898', 4, null, null, null, null, 1, null, 'biz_module', 4, 0, 0, null, '', null, null, null, null),
        (2063, '1548901111999773979', '重置密码', '1571131544973967361', 4, null, null, null, null, 1, null, 'biz_module', 5, 0, 0, null, '', null, null, null, null),
        (2064, '1548901111999773979', '删除人员', '1571131727656878081', 4, null, null, null, null, 1, null, 'biz_module', 6, 0, 0, null, '', null, null, null, null),
        (2065, '1548901111999773979', '启用禁用', '1571132076853657601', 4, null, null, null, null, 1, null, 'biz_module', 7, 0, 0, null, '', null, null, null, null),
        (2066, '1548901111999773980', '新增岗位', '1571132393993371649', 4, null, null, null, null, 1, null, 'biz_module', 1, 0, 0, null, '', null, null, null, null),
        (2067, '1548901111999773980', '批量删除', '1571132468178026497', 4, null, null, null, null, 1, null, 'biz_module', 2, 0, 0, null, '', null, null, null, null),
        (2068, '1548901111999773980', '编辑岗位', '1571132576143605761', 4, null, null, null, null, 1, null, 'biz_module', 3, 0, 0, null, '', null, null, null, null),
        (2069, '1548901111999773980', '删除岗位', '1571132658851086338', 4, null, null, null, null, 1, null, 'biz_module', 4, 0, 0, null, '', null, null, null, null),
        (2070, 'sys_module', '移动端管理', '1623378345382506498', 2, '/mobile', 'Layout', 'mobile-outlined', '', 1, null, 'sys_module', 50, 0, 0, null, '', null, null, '2025-01-28 01:26:04', null),
        (2071, '1623378345382506498', '模块管理', '1623378675591671810', 3, '/mobile/module/index', 'mobile/resource/module/index', 'build-outlined', null, 1, null, 'sys_module', 51, 0, 0, null, '', null, null, null, null),
        (2072, '1623378345382506498', '菜单管理', '1623378996099411969', 3, '/mobile/menu/index', 'mobile/resource/menu/index', 'appstore-add-outlined', null, 1, null, 'sys_module', 52, 0, 0, null, '', null, null, null, null),
        (2073, '1548901111999773979', '导出个人', '1635110416263262209', 4, null, null, null, null, 1, null, 'biz_module', 8, 0, 0, null, '', null, null, null, null),
        (2074, '1548901111999773979', '批量导出', '1635110536451043329', 4, null, null, null, null, 1, null, 'biz_module', 9, 0, 0, null, '', null, null, null, null),
        (2079, 'biz_module', '通知公告', '1811290937444667393', 3, '/biz/notice', 'biz/notice/index', 'appstore-outlined', null, 1, null, 'biz_module', 99, 0, 0, null, '', null, null, null, null),
        (2080, '1811290937444667393', '新增通知公告', '1811290937469833217', 4, null, null, null, null, 1, null, 'biz_module', 1, 0, 0, null, '', null, null, null, null),
        (2081, '1811290937444667393', '编辑通知公告', '1811290937486610434', 4, null, null, null, null, 1, null, 'biz_module', 2, 0, 0, null, '', null, null, null, null),
        (2082, '1811290937444667393', '删除通知公告', '1811290937494999042', 4, null, null, null, null, 1, null, 'biz_module', 3, 0, 0, null, '', null, null, null, null),
        (2083, '1811290937444667393', '批量删除', '1811290937503387650', 4, null, null, null, null, 1, null, 'biz_module', 4, 0, 0, null, '', null, null, null, null),
        (2084, '1811290937444667393', '更新状态', '1811328402989662210', 4, null, null, null, null, 1, null, 'biz_module', 5, 0, 0, null, '', null, null, null, null),
        (2085, '1811290937444667393', '通知公告详情', '1811330359695400961', 4, null, null, null, null, 1, null, 'biz_module', 6, 0, 0, null, '', null, null, null, null),
        (2128, '1548901111999770826', '查询列表', 'sys-org-list', 4, '', '', '', 'sys:org:list', 1, null, 'sys_module', 4, 0, 0, null, '', null, null, null, null),
        (2129, '1548901111999771226', '查看列表', 'DlXgS3UpyT', 4, null, null, '', 'sys:role:list', 1, null, 'sys_module', 99, 0, 0, null, '', null, null, null, null),
        (2130, '1548901111999770826', '增加组织', 'sys_org_add', 4, '', '', '', 'sys:org:add', 1, null, 'sys_module', 1, 0, 0, null, '', null, null, null, null),
        (2131, '1548901111999770826', '删除组织', 'sys_org_delete', 4, '', '', '', 'sys:org:delete', 1, null, 'sys_module', 2, 0, 0, null, '', null, null, null, null),
        (2132, '1548901111999770826', '修改组织', 'sys_org_edit', 4, '', '', '', 'sys:org:edit', 1, null, 'sys_module', 3, 0, 0, null, '', null, null, null, null),
        (2133, '1548901111999771426', '添加菜单', '67a33d00212cf7e944d10de8', 4, '', '', '', 'sys:menu:add', 1, null, 'sys_module', 9, 0, 0, null, '', '2025-02-05 18:27:12', null, '2025-02-05 18:27:12', null);

-- 组织机构
insert into moyu.sys_org (id, parent_code, name, code, org_type, org_level, sort_num, status, delete_flag, ext_json, remark, create_time, create_user, update_time, update_user)
values  (1768493150549356546, '0', 'MY集团', '10000000', 1, 1, 1, 0, 0, null, null, null, null, null, null),
        (1891803054210637826, '10000000', '集团总部', '67b46682d190ef73c4f9e6f0', 1, 2, 1, 0, 0, null, null, '2025-02-18 18:52:51', null, '2025-02-18 18:52:51', null),
        (1892491719769419777, '10000000', '北京分公司', '67b6e7e14c7494f1afc1f80c', 1, 2, 2, 0, 0, null, null, '2025-02-20 16:29:21', null, '2025-02-20 16:29:21', null),
        (1892491792502845442, '10000000', '东北公司', '67b6e7f24c7494f1afc1f80d', 1, 2, 3, 0, 0, null, null, '2025-02-20 16:29:39', null, '2025-02-20 16:29:39', null),
        (1892491865085276162, '10000000', '华东公司', '67b6e8044c7494f1afc1f80e', 1, 2, 4, 0, 0, null, null, '2025-02-20 16:29:56', null, '2025-02-20 16:30:11', null);

-- 分组
insert into moyu.sys_group (id, name, code, group_type, org_code, org_name, org_chain, sort_num, status, ext_json, remark, delete_flag, create_time, create_user, update_time, update_user)
values  (1, '超级管理员', '6784d936e1d6502bc7db738d', 1, '10000000', '集团总部', '10000000,0', 1, 0, null, null, 0, '2025-01-13 17:13:27', null, '2025-01-13 17:13:27', null);

-- 角色
insert into moyu.sys_role (id, name, code, module, data_scope, sort_num, status, ext_json, remark, delete_flag, create_time, create_user, update_time, update_user)
values  (100, '超级管理员', 'superadmin', null, 0, null, 0, null, null, 0, null, null, null, null);

-- 用户
insert into moyu.sys_user (id, account, password, nick_name, avatar, name, gender, birthday, email, phone, id_no, address, staff_code, entry_date, org_code, org_name, org_chain, login_ip, login_time, last_login_ip, last_login_time, pwd_update_time, status, remark, delete_flag, create_time, create_user, update_time, update_user)
values  (1, 'superAdmin', '$2a$10$ZxsW23u3p2wdnEpPTkT5zuOU.rs.TqyWAAa5eFTgxbQfbQggZ2Y3C', null, null, 'superAdmin', 1, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, 0, null, null, null, null);

-- 关系
insert into moyu.sys_relation (id, object_id, target_id, relation_type, create_time, create_user)
values  (1, '6784d936e1d6502bc7db738d', 'superAdmin', 1, '2025-02-20 16:39:25', null),
        (2, '6784d936e1d6502bc7db738d', 'superadmin', 2, '2025-02-20 16:39:25', null),
        (3, 'superadmin', '1548901111999770726', 3, '2025-02-20 16:39:25', null),
        (4, 'superadmin', '1548901111999770826', 3, '2025-02-20 16:39:25', null),
        (5, 'superadmin', '1548901111999770926', 3, '2025-02-20 16:39:25', null),
        (6, 'superadmin', '1548901111999771026', 3, '2025-02-20 16:39:25', null),
        (7, 'superadmin', '1548901111999771126', 3, '2025-02-20 16:39:25', null),
        (8, 'superadmin', '1548901111999771226', 3, '2025-02-20 16:39:25', null),
        (9, 'superadmin', '1548901111999771326', 3, '2025-02-20 16:39:25', null),
        (10, 'superadmin', '1548901111999771426', 3, '2025-02-20 16:39:25', null);
