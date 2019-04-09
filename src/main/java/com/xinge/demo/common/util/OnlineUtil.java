package com.xinge.demo.common.util;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.core.exception.user.UserNotOnlineException;
//import com.xinge.demo.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

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
    /*public static User getUser() {
        HttpServletRequest request = RequestUtil.getRequest();
        if (request == null) {
            throw new UserNotOnlineException();
        }
        Object user = request.getSession().getAttribute(StringConstant.SESSION_USER);
        if (user instanceof User && ((User) user).getId() != null) {
            return (User) user;
        }
        throw new UserNotOnlineException();
    }*/

}
