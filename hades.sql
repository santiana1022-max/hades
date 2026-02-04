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