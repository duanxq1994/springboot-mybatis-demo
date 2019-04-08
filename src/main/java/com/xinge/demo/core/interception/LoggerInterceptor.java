package com.xinge.demo.core.interception;

import com.google.gson.Gson;
import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.DebugHelper;
import com.xinge.demo.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 日志打印拦截器
 *
 * @author duanxq
 */
@Slf4j
public class LoggerInterceptor extends HandlerInterceptorAdapter {

    private static final int REQUEST_PARAM_SIZE = 2000;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI();
        if (requestUri != null && handler instanceof HandlerMethod && log.isInfoEnabled()) {
            Map<String, String> params = RequestUtil.request2Map(request);
            Map debugMap = DebugHelper.filterField(params);
            log.info("--> HttpRequest [{}:{}]:[{}]:{}", RequestUtil.getIpAddress(request), request.getSession().getId(), request.getRequestURI(), debugMap);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        //
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 输出HTTP返回信息
        String requestUri = request.getRequestURI();
        if (requestUri != null && handler instanceof HandlerMethod && log.isInfoEnabled()) {
            Object resultMap = request.getAttribute(StringConstant.REQUEST_MAP);
            String debugParam = "";
            if (resultMap != null) {
                debugParam = new Gson().toJson(resultMap);
                if (debugParam.length() > REQUEST_PARAM_SIZE) {
                    debugParam = debugParam.substring(0, REQUEST_PARAM_SIZE) + "......";
                }
            }
            log.info("<-- HttpResponse [{}:{}]:[{}]:{}", RequestUtil.getIpAddress(request), request.getSession().getId(), request.getRequestURI(), debugParam);
        }
    }

}
