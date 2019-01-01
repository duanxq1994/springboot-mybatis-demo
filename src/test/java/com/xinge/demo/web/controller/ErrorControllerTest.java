package com.xinge.demo.web.controller;

import com.xinge.demo.Application;
import com.xinge.demo.common.util.JsonUtil;
import com.xinge.demo.core.exception.ErrorCode;
import com.xinge.demo.model.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author duanxq
 * @date ${Date}
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ErrorControllerTest {

    private RestTemplate template = new RestTemplate();

    @Value("${server.port}")
    private String port;


    @Test
    public void error() {

        String url = "http://localhost:{port}/error/test";
        ResultEntity s = template.postForObject(url, null, ResultEntity.class, port);
        log.info(JsonUtil.objectToJson(s));
        Assert.assertEquals(ErrorCode.BIZ_ERROR.getCode(), s.getCode());
        Assert.assertEquals("error test!好", s.getError());
        Assert.assertEquals("error test!好", s.getMsg());
    }
}