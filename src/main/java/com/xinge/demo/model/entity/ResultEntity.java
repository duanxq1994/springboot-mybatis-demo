package com.xinge.demo.model.entity;

import lombok.Data;

/**
 * 返回实体
 *
 * @author duanx
 * @date 2019-1-19
 */
@Data
public class ResultEntity<T> {

    public static final int SUCCESS = 200;

    public ResultEntity() {

    }

    public ResultEntity(Integer code) {
        this.code = code;
    }

    private Integer code;

    private String message;

    private String error;

    private T data;

}
