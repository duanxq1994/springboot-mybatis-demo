package com.xinge.demo.core.interception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.CookieUtil;
import com.xinge.demo.common.util.DebugHelper;
import com.xinge.demo.common.util.MapUtil;
import com.xinge.demo.common.util.RequestUtil;
import com.xinge.demo.core.annotation.Backend;
import com.xinge.demo.model.domain.AdminerDO;
import com.xinge.demo.model.entity.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 日志打印拦截器
 * @author duanxq
 */
public class LoggerInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    private final static int REQUEST_PARAM_SIZE = 2000;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI();
        if (requestUri != null && requestUri.endsWith(".json") && handler instanceof HandlerMethod && logger.isInfoEnabled()) {
            Map<String, String> params = RequestUtil.request2Map(request);
            Map debugMap = DebugHelper.filterField(params);
            boolean isBackend = ((HandlerMethod) handler).getBeanType().isAnnotationPresent(Backend.class);
            String userInfo = getSessionUserInfo(request, isBackend);
            logger.info(String.format("--> HttpRequest [%s:%s:%s]:%s[%s]:%s", request.getRemoteHost(), request.getRemotePort(), request.getRequestedSessionId(), request.getRequestURI(), userInfo, debugMap));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 输出HTTP返回信息
        String requestUri = request.getRequestURI();
        if (requestUri != null && requestUri.endsWith(".json") && handler instanceof HandlerMethod && logger.isInfoEnabled()) {
            ResultEntity resultMap = (ResultEntity) request.getAttribute(StringConstant.REQUEST_MAP);
            boolean isBackend = ((HandlerMethod) handler).getBeanType().isAnnotationPresent(Backend.class);
            String userInfo = getSessionUserInfo(request, isBackend);
            String debugParam = "";
            if (resultMap != null) {
                //对resultEntity里的obj进行关键字过滤
                Map debugMap = DebugHelper.filterField(MapUtil.beanToMap(resultMap.getObject()));
                if (debugMap != null) {
                    resultMap.setObject(debugMap);
                }
                //忽略resultEntity里的list，仅显示list的size
                if (resultMap.getList() != null) {
                    resultMap.put("list", "size:" + resultMap.getList().size());
                }
                if ((debugParam = resultMap.toString()).length() > REQUEST_PARAM_SIZE) {
                    debugParam = debugParam.substring(0, REQUEST_PARAM_SIZE) + "......";
                }
            }
            logger.info(String.format("<-- HttpResponse[%s:%s:%s]:%s[%s]:%s", request.getRemoteHost(), request.getRemotePort(), request.getRequestedSessionId(), request.getRequestURI(), userInfo, debugParam));
        }
    }

    /**
     * 获取登陆用户的ID
     *
     * @param request   request
     * @param isBackend 是否为后台请求
     * @return
     */
    private String getSessionUserInfo(HttpServletRequest request, boolean isBackend) {
        if (isBackend) {
            AdminerDO adminerDO = (AdminerDO) request.getSession().getAttribute(StringConstant.SESSION_USER);
            return String.format("admin:%s", adminerDO == null ? "" : adminerDO.getId());
        } else {
            String companyId = CookieUtil.getCookieValue(request, StringConstant.COOKIE_COMPANY_ID);
            String employeeId = CookieUtil.getCookieValue(request, StringConstant.COOKIE_EMPLOYEE_ID);
            return String.format("Company:%s,Employee:%s", companyId, employeeId);
        }
    }
}
