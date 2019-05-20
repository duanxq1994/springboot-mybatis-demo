package com.xinge.demo.model;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
public class User {
    @Id
    private Integer id;

    /**
     * 账户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 状态，0：禁用，1：启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 账户类型，1：管理员，2：超级管理员
     */
    private Integer type;

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
     * 用于修改密码
     */
    private String token;

    /**
     * token过期时间
     */
    @Column(name = "token_expire_time")
    private Date tokenExpireTime;

    /**
     * 是否已经删除，0：未删除，1：已删除
     */
    private Integer deleted;
}