package com.xinge.demo.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author duanxq
 * @date 2019/2/23
 */
@Data
public class SuccessResult {

    @ApiModelProperty(example = "200")
    private Integer code = 200;

}
