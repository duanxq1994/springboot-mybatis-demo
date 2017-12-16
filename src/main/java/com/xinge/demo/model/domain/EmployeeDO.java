package com.xinge.demo.model.domain;

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
@Table(name = "employee")
public class EmployeeDO {
    /**
     * 自增主键
     */
    @Id
    private Long id;

    /**
     * 安保员工id（臻总管提供，包括、业务员、审核员、总账号）
     */
    private Long employeeId;

    /**
     * 安保员工姓名
     */
    private String employeeName;

    /**
     * 所属安保公司
     */
    private Long companyId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 员工类型( 1:业务员，2:审核员，3：总账号)
     */
    private Byte employeeType;

    /**
     * 身份证号,只有审核员有
     */
    private String idCard;

    /**
     * 审核员状态( 0:禁用.1:启用)
     */
    private Byte auditorStatus;

    private Date updated;

    private Date created;

}