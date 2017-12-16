package com.xinge.demo.model.domain;

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
@Table(name = "product")
public class ProductDO {
    /**
     * 商品编号
     */
    @Id
    private String productNo;

    /**
     * 商品名称
     */
    private String productName;

    private Date updated;

    private Date created;

}