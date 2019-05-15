package com.xinge.demo.service;

import com.xinge.demo.core.entity.PageDO;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author BG343674
 * created by BG343674 on 2019/4/8
 */
public interface IService<T> {

    /**
     * 新增
     *
     * @param obj
     * @return
     */
    void add(T obj);

    /**
     * 删除
     *
     * @param key
     * @return
     */
    void del(Object key);

    /**
     * 根据主键编辑
     *
     * @param obj
     * @return
     */
    void editByPK(T obj);

    /**
     * 根据条件编辑
     *
     * @param obj     需要编辑的内容
     * @param example 编辑条件
     */
    void editByExample(T obj, Example example);

    /**
     * 根据主键查询
     *
     * @param key
     * @return
     */
    T queryByPK(Object key);

    /**
     * 根据条件查询list
     *
     * @param obj
     * @return
     */
    List<T> queryForList(T obj);

    /**
     * 根据条件count
     *
     * @param obj
     * @return
     */
    Integer queryForCount(T obj);

    /**
     * 分页查询list
     *
     * @param obj
     * @param pageDO
     * @return
     */
    List<T> queryForPageList(T obj, PageDO pageDO);

}
