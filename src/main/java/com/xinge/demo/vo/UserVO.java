package com.xinge.demo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author duanxq
 * Created by duanxq on 2019-05-03.
 */
@Data
public class UserVO {

    @ApiModelProperty("")
    private Integer id;
    @ApiModelProperty("账户名")
    private String name;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("盐值")
    private String salt;
    @ApiModelProperty("状态，0：禁用，1：启用")
    private Integer status;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("账户类型，1：管理员，2：超级管理员")
    private Integer type;
    @ApiModelProperty("版本号")
    private Integer version;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("用于修改密码")
    private String token;
    @ApiModelProperty("token过期时间")
    private Date tokenExpireTime;
    @ApiModelProperty("是否已经删除，0：未删除，1：已删除")
    private Integer deleted;

}

