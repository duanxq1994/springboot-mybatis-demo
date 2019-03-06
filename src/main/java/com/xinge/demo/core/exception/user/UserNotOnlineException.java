package com.xinge.demo.core.exception.user;


import com.xinge.demo.core.exception.BizException;
import com.xinge.demo.core.exception.ErrorCode;

/**
 * @author BG343674
 * created by BG343674 on 2019/2/22
 */
public class UserNotOnlineException extends BizException {

    public UserNotOnlineException() {
        super(ErrorCode.USER_NOT_ONLINE.getCode(), ErrorCode.USER_NOT_ONLINE.getMsg());
    }
}
