package com.xinge.demo.core.exception.user;

import com.xinge.demo.core.exception.BizException;
import com.xinge.demo.core.exception.ErrorCode;

/**
 * @author duanxq
 * created by duanxq on 2019/2/22
 */
public class UserNotOnlineException extends BizException {

    public UserNotOnlineException() {
        super(ErrorCode.USER_NOT_ONLINE.getCode(), ErrorCode.USER_NOT_ONLINE.getMsg());
    }
}
