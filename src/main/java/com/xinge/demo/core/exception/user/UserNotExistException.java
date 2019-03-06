package com.xinge.demo.core.exception.user;

import com.xinge.demo.core.exception.BizException;

/**
 * @author duanxq
 * @date 2019/2/23
 */
public class UserNotExistException extends BizException {

    public UserNotExistException() {
        super("用户不存在");
    }
}
