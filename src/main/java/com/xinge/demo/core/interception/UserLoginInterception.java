package com.xinge.demo.core.interception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.CookieUtil;
import com.xinge.demo.core.annotation.Backend;
import com.xinge.demo.core.annotation.Login;
import com.xinge.demo.core.exception.BizException;
import com.xinge.demo.model.domain.AdminerDO;
import com.xinge.demo.model.entity.ResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author duanxq
 * @date 2017/4/17
 */
public class UserLoginInterception extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }
        //是否需要登陆
        HandlerMethod method = (HandlerMethod) handler;
        if (!methodNeedLogin(method)) {
            return super.preHandle(request, response, handler);
        }
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
        return super.preHandle(request, response, handler);
    }

    /**
     * 方法是否需要登录
     *
     * @param method method
     * @return isNeedLogin
     */
    private boolean methodNeedLogin(HandlerMethod method) {
        if (method.getMethodAnnotation(Login.class) != null) {
            return method.getMethodAnnotation(Login.class).value();
        } else {
            return method.getBeanType().isAnnotationPresent(Login.class) && method.getBeanType().getAnnotation(Login.class).value();
        }
    }


}
