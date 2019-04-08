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

    private String message;

    private String error;

    private T data;

}
