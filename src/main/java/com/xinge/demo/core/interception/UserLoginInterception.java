package com.xinge.demo.core.interception;

import com.xinge.demo.core.annotation.Login;
import com.xinge.demo.core.exception.user.UserNotOnlineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author duanxq
 * @date 2017/4/17
 */
@Slf4j
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
        if (!userIsLogin(request, method)) {
            throw new UserNotOnlineException();
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 用户是否已经登录
     *
     * @param request
     * @return
     */
    private boolean userIsLogin(HttpServletRequest request, HandlerMethod handlerMethod) {
        //
        log.warn("Code is not implemented");
        return true;
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
        }
        return method.getBeanType().isAnnotationPresent(Login.class) && method.getBeanType().getAnnotation(Login.class).value();
    }


}
