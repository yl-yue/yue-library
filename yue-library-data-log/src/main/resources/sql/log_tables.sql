-- 登录日志表
CREATE TABLE IF NOT EXISTS `log_login` (
  `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '有序主键',
  `username`        varchar(64)     NOT NULL DEFAULT '' COMMENT '登录账号',
  `ip`              varchar(64)     NOT NULL DEFAULT '' COMMENT '登录IP',
  `login_time`      bigint unsigned NOT NULL COMMENT '登录时间',
  `status`          tinyint         NOT NULL DEFAULT '0' COMMENT '登录状态：0=失败，1=成功',
  `msg`             varchar(512)    NOT NULL DEFAULT '' COMMENT '登录消息',
  `request_param`   text                     COMMENT '请求参数（脱敏后JSON）',
  `create_user`     varchar(60)     NOT NULL DEFAULT '' COMMENT '创建人：用户名、昵称、人名',
  `create_user_id`  bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人：用户id',
  `create_time`     bigint unsigned NOT NULL COMMENT '创建时间',
  `tenant_sys_id`   varchar(36)     NOT NULL DEFAULT '' COMMENT '系统租户：一级租户',
  `tenant_co_id`    varchar(36)     NOT NULL DEFAULT '' COMMENT '企业租户：二级租户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_username` (`username`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='登录日志';

-- 操作日志表
CREATE TABLE IF NOT EXISTS `log_oper` (
  `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '有序主键',
  `title`           varchar(128)    NOT NULL DEFAULT '' COMMENT '操作标题',
  `description`     varchar(512)    NOT NULL DEFAULT '' COMMENT '操作描述',
  `biz_type`        varchar(64)     NOT NULL DEFAULT '' COMMENT '业务分类',
  `oper_type`       varchar(2)      NOT NULL DEFAULT 'R' COMMENT '操作类型：C=新增，R=查询，U=更新，D=删除',
  `username`        varchar(64)     NOT NULL DEFAULT '' COMMENT '操作账号',
  `ip`              varchar(64)     NOT NULL DEFAULT '' COMMENT '操作IP',
  `oper_time`       bigint unsigned NOT NULL COMMENT '操作时间',
  `request_url`     varchar(512)    NOT NULL DEFAULT '' COMMENT '请求接口URL',
  `request_method`  varchar(16)     NOT NULL DEFAULT '' COMMENT '请求方法',
  `request_param`   text                     COMMENT '请求参数（脱敏后JSON）',
  `response_result` text                     COMMENT '响应结果（JSON）',
  `status`          tinyint         NOT NULL DEFAULT '0' COMMENT '操作状态：0=失败，1=成功',
  `create_user`     varchar(60)     NOT NULL DEFAULT '' COMMENT '创建人：用户名、昵称、人名',
  `create_user_id`  bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人：用户id',
  `create_time`     bigint unsigned NOT NULL COMMENT '创建时间',
  `tenant_sys_id`   varchar(36)     NOT NULL DEFAULT '' COMMENT '系统租户：一级租户',
  `tenant_co_id`    varchar(36)     NOT NULL DEFAULT '' COMMENT '企业租户：二级租户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_username` (`username`),
  KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志';

-- 登录日志归档表
CREATE TABLE IF NOT EXISTS `log_login_archive` (
  `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '有序主键',
  `username`        varchar(64)     NOT NULL DEFAULT '' COMMENT '登录账号',
  `ip`              varchar(64)     NOT NULL DEFAULT '' COMMENT '登录IP',
  `login_time`      bigint unsigned NOT NULL COMMENT '登录时间',
  `status`          tinyint         NOT NULL DEFAULT '0' COMMENT '登录状态：0=失败，1=成功',
  `msg`             varchar(512)    NOT NULL DEFAULT '' COMMENT '登录消息',
  `request_param`   text                     COMMENT '请求参数（脱敏后JSON）',
  `create_user`     varchar(60)     NOT NULL DEFAULT '' COMMENT '创建人：用户名、昵称、人名',
  `create_user_id`  bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人：用户id',
  `create_time`     bigint unsigned NOT NULL COMMENT '创建时间',
  `tenant_sys_id`   varchar(36)     NOT NULL DEFAULT '' COMMENT '系统租户：一级租户',
  `tenant_co_id`    varchar(36)     NOT NULL DEFAULT '' COMMENT '企业租户：二级租户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='登录日志归档';

-- 操作日志归档表
CREATE TABLE IF NOT EXISTS `log_oper_archive` (
  `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '有序主键',
  `title`           varchar(128)    NOT NULL DEFAULT '' COMMENT '操作标题',
  `description`     varchar(512)    NOT NULL DEFAULT '' COMMENT '操作描述',
  `biz_type`        varchar(64)     NOT NULL DEFAULT '' COMMENT '业务分类',
  `oper_type`       varchar(2)      NOT NULL DEFAULT 'R' COMMENT '操作类型：C=新增，R=查询，U=更新，D=删除',
  `username`        varchar(64)     NOT NULL DEFAULT '' COMMENT '操作账号',
  `ip`              varchar(64)     NOT NULL DEFAULT '' COMMENT '操作IP',
  `oper_time`       bigint unsigned NOT NULL COMMENT '操作时间',
  `request_url`     varchar(512)    NOT NULL DEFAULT '' COMMENT '请求接口URL',
  `request_method`  varchar(16)     NOT NULL DEFAULT '' COMMENT '请求方法',
  `request_param`   text                     COMMENT '请求参数（脱敏后JSON）',
  `response_result` text                     COMMENT '响应结果（JSON）',
  `status`          tinyint         NOT NULL DEFAULT '0' COMMENT '操作状态：0=失败，1=成功',
  `create_user`     varchar(60)     NOT NULL DEFAULT '' COMMENT '创建人：用户名、昵称、人名',
  `create_user_id`  bigint unsigned NOT NULL DEFAULT '0' COMMENT '创建人：用户id',
  `create_time`     bigint unsigned NOT NULL COMMENT '创建时间',
  `tenant_sys_id`   varchar(36)     NOT NULL DEFAULT '' COMMENT '系统租户：一级租户',
  `tenant_co_id`    varchar(36)     NOT NULL DEFAULT '' COMMENT '企业租户：二级租户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志归档';
