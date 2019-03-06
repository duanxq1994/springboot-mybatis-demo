package com.xinge.demo.common.util;

import com.alibaba.fastjson.JSON;
import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.core.exception.user.UserNotOnlineException;
import com.xinge.demo.model.domain.Member;
import com.xinge.demo.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author duanxq
 * @date 2019/2/17
 */
@Slf4j
public class OnlineUtil {

    /**
     * 获取在线前台用户
     *
     * @return
     */
    public static Member getMember() {
        HttpServletRequest request = RequestUtil.getRequest();
        if (request == null) {
            throw new UserNotOnlineException();
        }
        String aesToken = CookieUtil.getCookieValue(request, StringConstant.SESSION_MEMBER);
        if (StringUtils.isEmpty(aesToken)) {
            throw new UserNotOnlineException();
        }
        String aesKey = PropertiesUtil.getPropertiesValue("${aes.key}");
        String decryptValue = EncryptUtil.decryptValue(aesKey, aesToken);
        try {
            return JSON.parseObject(decryptValue, Member.class);
        } catch (Exception e) {
            log.warn("token[{}]解析异常：", decryptValue, e);
            throw new UserNotOnlineException();
        }
    }

    /**
     * 获取在线后台用户
     *
     * @return
     */
    public static User getUser() {
        HttpServletRequest request = RequestUtil.getRequest();
        if (request == null) {
            throw new UserNotOnlineException();
        }
        Object user = request.getSession().getAttribute(StringConstant.SESSION_USER);
        if (user instanceof User && ((User) user).getId() != null) {
            return (User) user;
        }
        throw new UserNotOnlineException();
    }

}
