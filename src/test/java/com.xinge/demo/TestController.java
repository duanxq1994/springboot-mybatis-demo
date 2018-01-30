package com.xinge.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author duanxq
 * @date 2018/1/30
 */
@RestController
public class TestController {

    @RequestMapping("/test/{id}")
    public Object test(@PathVariable String id, @MatrixVariable int q) {
        System.out.println(id);
//        System.out.println(map.toString());
        return ResponseEntity.ok();
    }

}
