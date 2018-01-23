package com.xinge.demo.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author duanxq
 * @date 2017/5/19
 */
public class CookieUtil {

    /**
     * 设置cookie过期时间，默认一年
     */
    private static final int AGE = 60 * 60 * 24 * 365;
    /**
     * 默认httpOnly
     */
    private static final boolean HTTP_ONLY = true;

    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        addCookie(response, name, value, AGE, HTTP_ONLY);
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     * @param age
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int age) {
        addCookie(response, name, value, age, HTTP_ONLY);
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     * @param httpOnly
     */
    public static void addCookie(HttpServletResponse response, String name, String value, boolean httpOnly) {
        addCookie(response, name, value, AGE, httpOnly);
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     * @param age
     * @param httpOnly
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int age, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        //设置cookie过期时间。0，立即删除。负数，浏览器关闭时自动删除
        cookie.setMaxAge(age);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    /**
     * 获取cookie值
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (cookieName != null) {
            Cookie cookie = getCookie(request, cookieName);
            if (cookie != null) {
                return cookie.getValue();
            }
        }
        return "";
    }

    private static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param cookieName
     */
    public static void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookies = new Cookie(cookieName, "");
        cookies.setPath("/");
        cookies.setMaxAge(0);
        response.addCookie(cookies);
    }

    public static void main(String[] args) {
//      CookieUtil util=new CookieUtil(request,response,-1);
//      util.addCookie("name","value");
//      String value=util.getCookieValue("name");
//      System.out.println("value="+value);
    }

}
