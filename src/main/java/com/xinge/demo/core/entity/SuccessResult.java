package com.xinge.demo.core.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author duanxq
 * @date 2019/2/23
 */
@Data
public class SuccessResult {

    public static final int SUCCESS = 200;

    @ApiModelProperty(value = "SUCCESS", example = "200")
    private Integer code = SUCCESS;

}
