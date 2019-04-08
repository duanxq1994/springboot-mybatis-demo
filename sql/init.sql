CREATE TABLE `member`
(
  `id`                int(11)        NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name`              varchar(255)   NOT NULL COMMENT '用户名',
  `mobile`            varchar(255)   NOT NULL COMMENT '手机号',
  `email`             varchar(255)   NOT NULL COMMENT '邮箱地址',
  `password`          varchar(255)   NOT NULL COMMENT '用户密码',
  `password_salt`     varchar(255)   NOT NULL COMMENT '密码盐值',
  `type`              int(11)        NOT NULL COMMENT '用户类型，0：账号，1：QQ登录，2：微信登录',
  `amount`            decimal(19, 2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
  `version`           int(11)        NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time`       datetime       NOT NULL COMMENT '创建时间',
  `token`             varchar(255)            DEFAULT NULL COMMENT '用户修改密码',
  `token_expire_time` datetime                DEFAULT NULL COMMENT 'token过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='前台用户表';

CREATE TABLE `user`
(
  `id`                int(11)      NOT NULL AUTO_INCREMENT,
  `name`              varchar(255) NOT NULL COMMENT '账户名',
  `password`          varchar(255) NOT NULL COMMENT '密码',
  `salt`              varchar(255) NOT NULL COMMENT '盐值',
  `status`            int(11)      NOT NULL COMMENT '状态，0：禁用，1：启用',
  `remark`            varchar(255)          DEFAULT NULL COMMENT '备注',
  `email`             varchar(255) NOT NULL COMMENT '邮箱',
  `mobile`            varchar(255)          DEFAULT NULL COMMENT '手机号',
  `type`              int(11)      NOT NULL DEFAULT '1' COMMENT '账户类型，1：管理员，2：超级管理员',
  `version`           int(11)      NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time`       datetime     NOT NULL COMMENT '创建时间',
  `token`             varchar(255)          DEFAULT NULL COMMENT '用于修改密码',
  `token_expire_time` datetime              DEFAULT NULL COMMENT 'token过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='后台用户表';