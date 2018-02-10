package com.xinge.demo.controller;

import com.xinge.demo.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author duanxq
 * @date 2018/1/31
 */
@RestController
public class ControllerTest {

    @Autowired
    private BankService bankService;

    @RequestMapping("{id}")
    public Object test(@PathVariable String id) {

        return ResponseEntity.ok(id);
    }

}
