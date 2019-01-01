package com.xinge.demo.web.controller;

import com.xinge.demo.core.exception.BizException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author duanxq
 * @date ${Date}
 */
@RestController
public class ErrorController {


    @RequestMapping("error/test")
    public Object error() {
        throw new BizException("error test!好");
    }

}
