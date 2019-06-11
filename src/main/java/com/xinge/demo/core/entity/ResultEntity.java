package com.xinge.demo.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 返回实体
 *
 * @author duanx
 * @date 2019-1-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResultEntity<T> extends SuccessResult {

    public ResultEntity() {
    }

    public ResultEntity(Integer code) {
        super.setCode(code);
    }

    public ResultEntity(Integer code, String message, String error) {
        this.setCode(code);
        this.setMessage(message);
        this.setError(error);
        this.setData(null);
    }

    public ResultEntity(Integer code, String message, String error, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setError(error);
        this.setData(data);
    }

    private String message;

    private String error;

    private T data;

}
