package com.xinge.demo.core.interception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.core.exception.BizException;
import com.xinge.demo.common.util.CookieUtil;
import com.xinge.demo.core.annotation.Backend;
import com.xinge.demo.core.annotation.Login;
import com.xinge.demo.model.domain.AdminerDO;
import com.xinge.demo.model.entity.ResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author duanxq
 * @date 2017/4/17
 */
public class UserLoginInterception extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            //是否需要登陆
            boolean needLogin = false;
            HandlerMethod method = (HandlerMethod) handler;
            if (method.getMethodAnnotation(Login.class) != null) {
                if (method.getMethodAnnotation(Login.class).value()) {
                    needLogin = true;
                }
            } else {
                if (method.getBeanType().isAnnotationPresent(Login.class) && method.getBeanType().getAnnotation(Login.class).value()) {
                    needLogin = true;
                }
            }
            if (needLogin) {
                boolean isBackend = ((HandlerMethod) handler).getBeanType().isAnnotationPresent(Backend.class);
                //是否已经登陆
                boolean isLogin = false;
                if (isBackend) {
                    //后台
                    Object adminer = request.getSession().getAttribute(StringConstant.SESSION_USER);
                    if (adminer instanceof AdminerDO) {
                        isLogin = true;
                    }
                } else {
                    //前台
                    String employeeId = CookieUtil.getCookieValue(request, StringConstant.COOKIE_EMPLOYEE_ID);
                    String companyId = CookieUtil.getCookieValue(request, StringConstant.COOKIE_COMPANY_ID);
                    String employeeType = CookieUtil.getCookieValue(request, StringConstant.COOKIE_EMPLOYEE_TYPE);
                    if (StringUtils.isNoneBlank(employeeId, companyId, employeeType)) {
                        isLogin = true;
                    }
                }
                if (!isLogin) {
                    throw new BizException(ResultEntity.NOT_LOGIN);
                }
            }
        }
        //response.setHeader("Access-Control-Allow-Origin", "*");
        return super.preHandle(request, response, handler);
    }


}
