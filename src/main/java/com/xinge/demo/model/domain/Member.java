package com.xinge.demo.model.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Member {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 密码盐值
     */
    @Column(name = "password_salt")
    private String passwordSalt;

    /**
     * 用户类型，0：账号，1：QQ登录，2：微信登录
     */
    private Integer type;

    /**
     * 账户余额
     */
    private BigDecimal amount;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 用户修改密码
     */
    private String token;

    /**
     * token过期时间
     */
    @Column(name = "token_expire_time")
    private Date tokenExpireTime;
}