package com.xinge.demo.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author duanxq
 */
@Getter
@Setter
@ToString
@Table(name = "bank")
public class BankDO {
    /**
     * 银行id
     */
    @Id
    private Long id;

    /**
     * 银行名称
     */
    private String name;

    /**
     * 银行图标地址
     */
    private String image;

    /**
     * 创建时间
     */
    @JsonIgnore
    private Date created;

    /**
     * 更新时间
     */
    @JsonIgnore
    private Date updated;

}