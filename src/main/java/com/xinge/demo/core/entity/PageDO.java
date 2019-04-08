package com.xinge.demo.core.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Transient;

/**
 * 针对分页显示
 *
 * @author duanxq
 * @date 2017/12/17
 */
@Data
public class PageDO {

    /**
     * 当前页码
     */
    @JsonIgnore
    @Transient
    private Integer pageNum;

    /**
     * 每页显示条数
     */
    @JsonIgnore
    @Transient
    private Integer pageSize;



    public Integer getPageSize() {
        if (this.pageSize == null) {
            //pageHelper pageSizeZero 参数设置为true，pageSize=0时，查询全部结果
            return 0;
        }
        return pageSize;
    }

    public Integer getPageNum() {
        if (this.pageNum == null) {
            return 1;
        }
        return pageNum;
    }
}
