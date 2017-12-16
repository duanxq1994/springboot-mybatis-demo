package com.xinge.demo.core.resolver;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.CookieUtil;
import com.xinge.demo.core.annotation.Backend;
import com.xinge.demo.core.annotation.Login;
import com.xinge.demo.model.domain.EmployeeDO;
import com.xinge.demo.model.domain.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息自动注入参数中
 *
 * @author duanxq
 * @date 2017/4/18
 */
public class SessionArgumentResolver implements HandlerMethodArgumentResolver {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Boolean needLogin = false;
        boolean isBackend = methodParameter.getContainingClass().getAnnotation(Backend.class) != null;
        if (methodParameter.getMethodAnnotation(Login.class) != null) {
            if (methodParameter.getMethodAnnotation(Login.class).value()) {
                needLogin = true;
            }
        } else if (methodParameter.getContainingClass().getAnnotation(Login.class) != null
                && methodParameter.getContainingClass().getAnnotation(Login.class).value()) {
            needLogin = true;
        }
        return needLogin
                && (isBackend ? User.class.isAssignableFrom(methodParameter.getParameterType())
                : EmployeeDO.class.isAssignableFrom(methodParameter.getParameterType()));
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        boolean isBackend = methodParameter.getContainingClass().getAnnotation(Backend.class) != null;
        if (isBackend) {
            Object sessionUser = nativeWebRequest.getAttribute(StringConstant.SESSION_USER, RequestAttributes.SCOPE_SESSION);
            User user = new User();
            try {
                BeanUtils.copyProperties(user, sessionUser);
            } catch (Exception e) {
                logger.warn("解析session中的用户信息异常：", e);
            }
            return user;
        } else {
            HttpServletRequest nativeRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            String employeeId = CookieUtil.getCookieValue(nativeRequest, StringConstant.COOKIE_EMPLOYEE_ID);
            String companyId = CookieUtil.getCookieValue(nativeRequest, StringConstant.COOKIE_COMPANY_ID);
            String employeeType = CookieUtil.getCookieValue(nativeRequest, StringConstant.COOKIE_EMPLOYEE_TYPE);
            EmployeeDO employeeDO = new EmployeeDO();
            if (StringUtils.isNoneBlank(employeeId, companyId, employeeType)) {
                try {
                    employeeDO.setEmployeeId(Long.valueOf(employeeId));
                    employeeDO.setCompanyId(Long.valueOf(companyId));
                    employeeDO.setEmployeeType(Byte.valueOf(employeeType));
                } catch (NumberFormatException e) {
                    logger.warn("解析cookie异常：", e);
                }
            }
            return employeeDO;
        }
    }
}
