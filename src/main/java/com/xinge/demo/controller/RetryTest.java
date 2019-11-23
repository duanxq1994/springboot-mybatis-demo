package com.xinge.demo.controller;

import com.xinge.demo.model.User;
import com.xinge.demo.service.RetryServiceTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

/**
 * @author BG343674
 * created by BG343674 on 2019/9/19
 */
@Slf4j
@RestController
@RequestMapping("retry")
public class RetryTest {

    @Autowired
    private RetryServiceTest retryServiceTest;



    @RequestMapping("b/{a}")
    public Object b(@PathParam("a") Integer a) throws Exception {
        User user = new User();
        user.setName("aaa");
        user.setMobile("13312341234");
        retryServiceTest.test(a, "2");
        return 3;


    }


}
