package com.xinge.demo.controller;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.core.entity.SuccessResult;
import com.xinge.demo.core.shiro.MyToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author duanxq
 * created by duanxq on 2019/6/11
 */
@Api(tags = "Login")
@RestController
@RequestMapping("admin/login")
public class LoginController {

    @PostMapping("login")
    @ApiOperation(value = "登录")
    public SuccessResult login(String userName, String password) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new MyToken(StringConstant.LOGIN_TYP_ADMIN, userName, password));
        return new SuccessResult();
    }

    @GetMapping("logout")
    @ApiOperation("登出")
    public SuccessResult logout() {
        SecurityUtils.getSubject().logout();
        return new SuccessResult();
    }

}
