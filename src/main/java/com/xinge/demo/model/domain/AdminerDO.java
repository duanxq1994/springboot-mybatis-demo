package com.xinge.demo.model.domain;

import com.xinge.demo.model.entity.PageDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author duanxq
 */
@Getter
@Setter
@ToString
@Table(name = "adminer")
public class AdminerDO extends PageDO {
    /**
     * 管理员id
     */
    @Id
    private Long id;

    /**
     * 管理员姓名
     */
    private String adminerName;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号,也就是手机号
     */
    private String account;

    /**
     * 角色( 0:超级管理员 1:管理员 2:安保公司员工 3:财务人员)
     */
    private Byte role;

    /**
     * 管理员状态( 0:禁用 1:启用)
     */
    private Byte adminerStatus;

    /**
     * 关联公司表
     */
    private Long companyId;

    private Date updated;

    private Date created;

}