package com.xinge.demo.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * request请求处理工具类
 */
public class RequestUtil {

    /**
     * 静态取request，本地junit非容器运行时无法使用，服务未启动完成无法使用
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            if (attr != null) {
                return attr.getRequest();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 取完整根路径，防止重复contextpath
     *
     * @param request
     * @param page
     * @return
     */
    public static String getRootURL(HttpServletRequest request, String page) {
        StringBuffer url = request.getRequestURL();
        String rootUrl = url.substring(0, url.indexOf(request.getRequestURI()));
        if (StringUtils.isEmpty(page)) {
            return rootUrl;
        }
        if (!rootUrl.endsWith("/") && !page.startsWith("/")) {
            rootUrl = rootUrl + "/";
        }
        return rootUrl + page;
    }

    /**
     * 取重定向完整根路径，防止重复contextpath
     *
     * @param request
     * @param page
     * @return
     */
    public static String getRootRedirectURL(HttpServletRequest request, String page) {
        StringBuffer url = request.getRequestURL();
        String redirect = url.substring(0, url.indexOf(request.getRequestURI()));

        String host = request.getHeader("Real-Host");
        if (StringUtils.isNotBlank(host)) {
            redirect = redirect.replace(request.getHeader("Host"), host);
        }
        if (redirect.startsWith("http:")) {
            String referer = request.getHeader("referer");
            if (StringUtils.isNotBlank(referer) && referer.startsWith("https")) {
                redirect = redirect.replace("http:", "https:");
            }
        }
        if (StringUtils.isEmpty(page)) {
            return "redirect:" + redirect;
        }
        if (!redirect.endsWith("/") && !page.startsWith("/")) {
            redirect = redirect + "/";
        }
        return "redirect:" + redirect + page;
    }

    /**
     * 取IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * @param request
     * @return
     * @category 获取request参数
     */
    public static Map<String, String> request2Map(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            String value = request.getParameter(name);
            if (request.getParameterValues(name) != null) {
                value = StringUtils.join(request.getParameterValues(name), ",");
            }
            map.put(name, value);
        }
        return map;
    }
}
