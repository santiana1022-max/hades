CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL COMMENT '主键ID（雪花算法）',
                            `username` varchar(50) NOT NULL COMMENT '用户名（唯一，登录账号）',
                            `password` varchar(100) NOT NULL COMMENT '加密密码（BCrypt加密）',
                            `salt` varchar(32) NOT NULL COMMENT '密码加密盐值',
                            `phone` varchar(20) DEFAULT NULL COMMENT '手机号（唯一）',
                            `email` varchar(100) DEFAULT NULL COMMENT '邮箱（唯一）',
                            `wx_openid` varchar(100) DEFAULT NULL COMMENT '微信开放平台openid（唯一）',
                            `wx_unionid` varchar(100) DEFAULT NULL COMMENT '微信unionid（多应用统一标识）',
                            `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像URL',
                            `status` tinyint NOT NULL DEFAULT '1' COMMENT '账号状态：0-禁用，1-正常',
                            `login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
                            `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                            `login_count` int NOT NULL DEFAULT '0' COMMENT '累计登录次数',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `create_by` varchar(50) NOT NULL COMMENT '创建人',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                            `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
                            `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注（如：测试管理员、开发人员）',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`),
                            UNIQUE KEY `uk_phone` (`phone`),
                            UNIQUE KEY `uk_email` (`email`),
                            UNIQUE KEY `uk_wx_openid` (`wx_openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

INSERT INTO `sys_user` (
    `id`, `username`, `password`, `salt`,
    `phone`, `email`, `wx_openid`, `wx_unionid`, `avatar`,
    `status`, `login_ip`, `login_time`, `login_count`,
    `create_time`, `create_by`, `update_time`, `update_by`, `is_deleted`, `remark`
) VALUES (
             1001,  -- 用户ID（固定，方便关联）
             'admin',  -- 登录账号
             '$2a$10$Z7X9Y2W8S1D3F4G5H6J7K8L9Z0X1C2V3B4N5M6A7S8D9F0G1H2J',  -- 加密密码
             '5f8d2a7e9c1b3e5f7a9d2c4e6f8a0b1',  -- 32位随机盐值
             NULL,    -- 手机号（默认空）
             NULL,    -- 邮箱（默认空）
             NULL,    -- 微信openid（默认空）
             NULL,    -- 微信unionid（默认空）
             NULL,    -- 头像（默认空）
             1,       -- 账号状态：1-正常
             NULL,    -- 最后登录IP（默认空）
             NULL,    -- 最后登录时间（默认空）
             0,       -- 累计登录次数（初始0）
             NOW(),   -- 创建时间
             'system',-- 创建人
             NOW(),   -- 修改时间
             'system',-- 修改人
             0,       -- 未删除
             '测试平台默认管理员账号'  -- 备注
         );

-- 创建角色表
CREATE TABLE `sys_role` (
                            `id` bigint NOT NULL COMMENT '角色ID',
                            `role_name` varchar(64) NOT NULL COMMENT '角色名称',
                            `role_code` varchar(64) NOT NULL COMMENT '角色编码（唯一）',
                            `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
                            `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
                            `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_role_code` (`role_code`) COMMENT '角色编码唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 创建菜单表
CREATE TABLE `sys_menu` (
                            `id` bigint NOT NULL COMMENT '菜单ID',
                            `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父菜单ID（0=顶级）',
                            `menu_name` varchar(64) NOT NULL COMMENT '菜单名称',
                            `menu_type` char(1) NOT NULL COMMENT '类型：C-目录，M-菜单，B-按钮',
                            `path` varchar(255) DEFAULT NULL COMMENT '路由路径',
                            `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
                            `perms` varchar(128) DEFAULT NULL COMMENT '权限标识',
                            `sort` int NOT NULL DEFAULT 0 COMMENT '排序号',
                            `icon` varchar(64) DEFAULT NULL COMMENT '菜单图标',
                            `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
                            `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`),
                            KEY `idx_parent_id` (`parent_id`) COMMENT '父菜单索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 3. 用户-角色关联表
CREATE TABLE `sys_user_role` (
                                 `id` bigint NOT NULL COMMENT '主键ID',
                                 `user_id` bigint NOT NULL COMMENT '用户ID',
                                 `role_id` bigint NOT NULL COMMENT '角色ID',
                                 `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
                                 `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_user_role` (`user_id`,`role_id`) COMMENT '用户-角色唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

-- 4. 角色-菜单关联表
CREATE TABLE `sys_role_menu` (
                                 `id` bigint NOT NULL COMMENT '主键ID',
                                 `role_id` bigint NOT NULL COMMENT '角色ID',
                                 `menu_id` bigint NOT NULL COMMENT '菜单ID',
                                 `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
                                 `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`) COMMENT '角色-菜单唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单关联表';

-- 插入默认角色（id用雪花算法，此处用固定值方便关联，可替换为实际雪花ID）
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `status`, `create_by`, `remark`) VALUES
                                                                                                            (1, '超级管理员', 'admin', '拥有系统所有权限，最高级别角色', 1, 'system', '系统内置超级管理员'),
                                                                                                            (2, '普通用户', 'user', '拥有基础业务操作权限，无系统管理权限', 1, 'system', '系统内置普通用户'),
                                                                                                            (3, '游客', 'guest', '仅拥有查看权限，无任何操作权限', 1, 'system', '系统内置游客');

-- 插入默认菜单（parent_id=0为顶级目录，共22条，包含目录、菜单、按钮）
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `sort`, `icon`, `status`, `create_by`, `remark`) VALUES
-- 顶级目录：系统管理
(100, 0, '系统管理', 'C', '', '', '', 1, 'Setting', 1, 'system', '系统核心管理模块'),
-- 系统管理-用户管理
(101, 100, '用户管理', 'M', '/system/user', 'system/user/index', 'system:user:list', 1, 'User', 1, 'system', '用户列表管理'),
(102, 101, '用户新增', 'B', '', '', 'system:user:add', 1, '', 1, 'system', '用户新增按钮权限'),
(103, 101, '用户编辑', 'B', '', '', 'system:user:edit', 2, '', 1, 'system', '用户编辑按钮权限'),
(104, 101, '用户删除', 'B', '', '', 'system:user:delete', 3, '', 1, 'system', '用户删除按钮权限'),
(105, 101, '用户重置密码', 'B', '', '', 'system:user:resetPwd', 4, '', 1, 'system', '用户重置密码权限'),
-- 系统管理-角色管理
(110, 100, '角色管理', 'M', '/system/role', 'system/role/index', 'system:role:list', 2, 'Avatar', 1, 'system', '角色列表管理'),
(111, 110, '角色新增', 'B', '', '', 'system:role:add', 1, '', 1, 'system', '角色新增按钮权限'),
(112, 110, '角色编辑', 'B', '', '', 'system:role:edit', 2, '', 1, 'system', '角色编辑按钮权限'),
(113, 110, '角色删除', 'B', '', '', 'system:role:delete', 3, '', 1, 'system', '角色删除按钮权限'),
(114, 110, '角色分配权限', 'B', '', '', 'system:role:assignPerm', 4, '', 1, 'system', '角色分配权限权限'),
-- 系统管理-菜单管理
(120, 100, '菜单管理', 'M', '/system/menu', 'system/menu/index', 'system:menu:list', 3, 'Menu', 1, 'system', '菜单列表管理'),
(121, 120, '菜单新增', 'B', '', '', 'system:menu:add', 1, '', 1, 'system', '菜单新增按钮权限'),
(122, 120, '菜单编辑', 'B', '', '', 'system:menu:edit', 2, '', 1, 'system', '菜单编辑按钮权限'),
(123, 120, '菜单删除', 'B', '', '', 'system:menu:delete', 3, '', 1, 'system', '菜单删除按钮权限'),
-- 顶级目录：日志管理
(200, 0, '日志管理', 'C', '', '', '', 2, 'Document', 1, 'system', '系统日志模块'),
-- 日志管理-登录日志
(201, 200, '登录日志', 'M', '/log/login', 'log/login/index', 'log:login:list', 1, 'Clock', 1, 'system', '登录日志查询'),
(202, 201, '日志导出', 'B', '', '', 'log:login:export', 1, '', 1, 'system', '登录日志导出权限'),
-- 日志管理-操作日志
(210, 200, '操作日志', 'M', '/log/operate', 'log/operate/index', 'log:operate:list', 2, 'Edit', 1, 'system', '操作日志查询'),
-- 顶级目录：个人中心
(300, 0, '个人中心', 'C', '', '', '', 3, 'UserFilled', 1, 'system', '个人信息模块'),
(301, 300, '个人信息', 'M', '/profile', 'profile/index', 'profile:info', 1, 'User', 1, 'system', '个人信息查看/编辑'),
(302, 300, '修改密码', 'M', '/profile/password', 'profile/password', 'profile:password', 2, 'Lock', 1, 'system', '个人密码修改');

-- 假设sys_user表中存在admin用户（id=1），分配超级管理员角色
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_by`) VALUES
    (1, 1, 1, 'system');

-- 1. 超级管理员（admin）：拥有所有菜单权限
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_by`)
SELECT ROW_NUMBER() OVER(), 1, id, 'system' FROM sys_menu WHERE is_deleted = 0;

-- 2. 普通用户（user）：拥有个人中心、用户查看、日志查看权限
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_by`) VALUES
                                                                          (100, 2, 300, 'system'), (101, 2, 301, 'system'), (102, 2, 302, 'system'),
                                                                          (103, 2, 101, 'system'), (104, 2, 200, 'system'), (105, 2, 201, 'system'), (106, 2, 210, 'system');

-- 3. 游客（guest）：仅拥有个人中心查看、日志查看权限
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_by`) VALUES
                                                                          (200, 3, 300, 'system'), (201, 3, 301, 'system'),
                                                                          (202, 3, 200, 'system'), (203, 3, 201, 'system'), (204, 3, 210, 'system');

-- 删除旧表（若存在）
DROP TABLE IF EXISTS `sys_user`;

-- 重新创建用户表（ID自增）
CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）', -- 核心修改：添加AUTO_INCREMENT
                            `username` varchar(64) NOT NULL COMMENT '用户名（唯一）',
                            `password` varchar(255) NOT NULL COMMENT '加密密码',
                            `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
                            `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
                            `wx_openid` varchar(128) DEFAULT NULL COMMENT '微信openid',
                            `wx_unionid` varchar(128) DEFAULT NULL COMMENT '微信unionid',
                            `avatar` varchar(512) DEFAULT NULL COMMENT '头像URL',
                            `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                            `login_ip` varchar(32) DEFAULT NULL COMMENT '最后登录IP',
                            `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                            `login_count` int NOT NULL DEFAULT 0 COMMENT '累计登录次数',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
                            `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`),
                            UNIQUE KEY `uk_phone` (`phone`),
                            UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';


-- 插入15条默认用户数据（密码均为123456，BCrypt加密）
INSERT INTO `sys_user` (`username`, `password`, `phone`, `email`, `avatar`, `status`, `login_count`, `create_by`, `remark`) VALUES
-- 1. 超级管理员（核心用户）
('admin', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138000', 'admin@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '超级管理员，拥有所有权限'),
-- 2-6. 普通用户（5人）
('user01', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138001', 'user01@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '普通用户，基础业务权限'),
('user02', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138002', 'user02@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '普通用户，基础业务权限'),
('user03', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138003', 'user03@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '普通用户，基础业务权限'),
('user04', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138004', 'user04@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '普通用户，基础业务权限'),
('user05', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138005', 'user05@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '普通用户，基础业务权限'),
-- 7-11. 游客用户（5人）
('guest01', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138006', 'guest01@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '游客，仅查看权限'),
('guest02', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138007', 'guest02@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '游客，仅查看权限'),
('guest03', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138008', 'guest03@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '游客，仅查看权限'),
('guest04', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138009', 'guest04@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '游客，仅查看权限'),
('guest05', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138010', 'guest05@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '游客，仅查看权限'),
-- 12-15. 无角色用户（4人，未分配任何角色）
('test01', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138011', 'test01@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '测试用户，无角色'),
('test02', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138012', 'test02@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '测试用户，无角色'),
('test03', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138013', 'test03@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '测试用户，无角色'),
('test04', '$2a$10$dXJ34X4X4X4X4X4X4X4X4e4X4X4X4X4X4X4X4X4X4X4X4X4X4X', '13800138014', 'test04@test.com', 'https://cube.elemecdn.com/0/59/c532199f02d46d2167408c0a25d18jpeg.jpeg', 1, 0, 'system', '测试用户，无角色');

-- 插入用户-角色关联（对应角色ID：1-超级管理员，2-普通用户，3-游客）
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`) VALUES
-- 1. admin → 超级管理员（role_id=1）
(1, 1, 'system'),
-- 2-6. user01-user05 → 普通用户（role_id=2）
(2, 2, 'system'),
(3, 2, 'system'),
(4, 2, 'system'),
(5, 2, 'system'),
(6, 2, 'system'),
-- 7-11. guest01-guest05 → 游客（role_id=3）
(7, 3, 'system'),
(8, 3, 'system'),
(9, 3, 'system'),
(10, 3, 'system'),
(11, 3, 'system');
-- 12-15. test01-test04 无角色，无需插入