package com.xinge.demo.core.shiro;

import com.xinge.demo.common.constant.StringConstant;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author BG343674
 * created by BG343674 on 2019/3/11
 */
public class MyToken extends UsernamePasswordToken {

    public MyToken(String loginType, String userName, String password) {
        super(userName, password);
        this.setLoginType(loginType);
    }

    /**
     * 登录类型 {@link StringConstant#LOGIN_TYP_ADMIN}
     */
    private String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
