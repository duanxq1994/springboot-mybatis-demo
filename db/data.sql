CREATE DATABASE IF NOT EXISTS `testdb` CHARACTER SET UTF8;
CREATE TABLE If NOT EXISTS `testdb`.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '账户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `salt` varchar(255) NOT NULL COMMENT '盐值',
  `status` int(11) NOT NULL COMMENT '状态，0：禁用，1：启用',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `mobile` varchar(255) DEFAULT NULL COMMENT '手机号',
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '账户类型，1：管理员，2：超级管理员',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `token` varchar(255) DEFAULT NULL COMMENT '用于修改密码',
  `token_expire_time` datetime DEFAULT NULL COMMENT 'token过期时间',
  `deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否已经删除，0：未删除，1：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
);

-- user:admin password:admin
INSERT INTO `testdb`.`user` (`id`, `name`, `password`, `salt`, `status`, `remark`, `email`, `mobile`, `type`, `version`, `create_time`, `token`, `token_expire_time`, `deleted`)
VALUES
  ('1', 'admin', 'ee288c8cb70eaa37f670ba077384b5e4', 'RaGapN', '1', NULL, '', '', '2', '5', '2019-02-23 12:00:00', NULL,
   NULL, '0');

CREATE TABLE `order_sequence`
(
  `date` varchar(8) NOT NULL COMMENT '日期',
  `num`  int(11)    NOT NULL DEFAULT '1' COMMENT '订单数量',
  PRIMARY KEY (`date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='每天订单数量表（用户生成订单号）';

INSERT INTO `testdb`.`order_sequence` (`date`, `num`) VALUES ('20190225', '2');
INSERT INTO `testdb`.`order_sequence` (`date`, `num`) VALUES ('20190303', '1');
INSERT INTO `testdb`.`order_sequence` (`date`, `num`) VALUES ('20190323', '1');
INSERT INTO `testdb`.`order_sequence` (`date`, `num`) VALUES ('20190324', '348');
