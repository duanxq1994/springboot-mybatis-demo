package com.xinge.demo.core.entity;

import lombok.Data;

/**
 * @author duanxq
 * @date 2019/2/23
 */
@Data
public class SuccessResult {

    public static final int SUCCESS = 200;

    private Integer code = SUCCESS;

}
