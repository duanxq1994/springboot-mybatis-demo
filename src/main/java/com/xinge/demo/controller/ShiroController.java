package com.xinge.demo.controller;

import com.xinge.demo.core.exception.user.UserNotOnlineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author duanxq
 * created by duanxq on 2019/5/20
 */
@Slf4j
@RestController
public class ShiroController {

    @RequestMapping("notLogin")
    public Object notLogin(HttpServletRequest request) {
        log.info(request.getRequestURI());
        throw new UserNotOnlineException();
    }

    @RequestMapping("unauthc")
    public Object unauthc(HttpServletRequest request) {
        log.warn(request.getRequestURI());
        throw new UserNotOnlineException();
    }

}
