package com.xinge.demo.common.util;

import com.xinge.demo.core.exception.user.UserNotOnlineException;
import com.xinge.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;


/**
 * @author duanxq
 * @date 2019/2/17
 */
@Slf4j
public class OnlineUtil {

    /**
     * 获取在线用户
     *
     * @return
     */
    public static User getUser() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new UserNotOnlineException();
    }

}
