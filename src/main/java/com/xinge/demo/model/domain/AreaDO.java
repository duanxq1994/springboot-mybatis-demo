package com.xinge.demo.model.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author duanxq
 */
@Getter
@Setter
@ToString
@Table(name = "area")
public class AreaDO {
    /**
     * 地区码
     */
    @Id
    private String areaCode;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 上级区域编码
     */
    private String parentCode;

    /**
     * 1国家2省3市4区5街道
     */
    private String areaType;

    /**
     * 对应省编码
     */
    private String rootCode;
}