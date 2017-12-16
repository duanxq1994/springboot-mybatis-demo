package com.xinge.demo.service;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.model.domain.ProductDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 产品service
 * created by duanxq on 2017/10/17
 */
@Service
public class ProductService extends BaseService<ProductDO> {


    /**
     * 通过商品编号查询商品名称，并进行缓存
     *
     * @param productNo 商品信息
     * @return
     */
    @Cacheable(value = "productCache")
    public String getProductNameByNo(String productNo) {
        if (StringUtils.isEmpty(productNo)) {
            //兼容之前版本，如果通过臻总管业务员提交的页面，默认产品编号，产品为联网报警系统
            return mapper.selectByPrimaryKey(StringConstant.PRODUCT_DEFAULT_VALUE).getProductName();
        }
        String productName = null;
        ProductDO productDO = mapper.selectByPrimaryKey(productNo);
        if (productDO != null) {
            productName = productDO.getProductName();
        }
        return productName;
    }

    /**
     * 编辑商品信息，并清除缓存
     *
     * @param productDO 商品信息
     */
    @CacheEvict(value = "productCache", key = "#productDO.getProductNo()")
    public void editProduct(ProductDO productDO) {
        mapper.updateByPrimaryKeySelective(productDO);
    }

    /**
     * 新增或编辑商品信息，并清除缓存
     *
     * @param productDO 商品信息
     */
    @CacheEvict(value = "productCache", key = "#productDO.getProductNo()")
    public void addOrUpdate(ProductDO productDO) {
        if (getProductNameByNo(productDO.getProductNo()) == null) {
            add(productDO);
        } else {
            editProduct(productDO);
        }
    }

}
