package com.xinge.demo.web.controller;

import com.xinge.demo.common.util.JsonUtil;
import com.xinge.demo.core.exception.ErrorCode;
import com.xinge.demo.model.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * @author duanxq
 * @date ${Date}
 */
@Slf4j
public class ErrorControllerTest {

    private RestTemplate template = new RestTemplate();

    @Test
    public void error() {
        String url = "http://localhost:8061/error/test";
        ResultEntity s = template.postForObject(url, null, ResultEntity.class);
        log.info(JsonUtil.objectToJson(s));
        Assert.assertEquals(ErrorCode.BIZ_ERROR.getCode(), s.getCode());
        Assert.assertEquals("error test!好", s.getError());
        Assert.assertEquals("error test!好", s.getMsg());
    }
}