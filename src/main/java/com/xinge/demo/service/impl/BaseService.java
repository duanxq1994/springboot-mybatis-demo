package com.xinge.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.xinge.demo.common.util.MyMapper;
import com.xinge.demo.core.entity.BatchResultDTO;
import com.xinge.demo.core.entity.PageDO;
import com.xinge.demo.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author duanxq
 * @date 2017/9/9
 */
public abstract class BaseService<T> implements IService<T> {

    @Autowired
    public MyMapper<T> mapper;

    protected Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 新增
     *
     * @param obj
     * @return
     */
    @Override
    public void add(T obj) {
        mapper.insertSelective(obj);
    }


    /**
     * 删除
     *
     * @param key
     * @return
     */
    @Override
    public void del(Object key) {
        mapper.deleteByPrimaryKey(key);
    }

    /**
     * 根据主键编辑
     *
     * @param obj
     * @return
     */
    @Override
    public void editByPK(T obj) {
        mapper.updateByPrimaryKeySelective(obj);
    }

    /**
     * 根据条件编辑
     *
     * @param obj     需要编辑的内容
     * @param example 编辑条件
     */
    @Override
    public void editByExample(T obj, Example example) {
        mapper.updateByExampleSelective(obj, example);
    }

    /**
     * 根据主键查询
     *
     * @param key
     * @return
     */
    @Override
    public T queryByPK(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    /**
     * 根据条件查询list
     *
     * @param obj
     * @return
     */
    @Override
    public List<T> queryForList(T obj) {
        return mapper.select(obj);
    }

    /**
     * 根据条件count
     *
     * @param obj
     * @return
     */
    @Override
    public Integer queryForCount(T obj) {
        return mapper.selectCount(obj);
    }

    /**
     * 分页查询list
     *
     * @param obj
     * @return
     */
    @Override
    public BatchResultDTO<T> queryForPageList(T obj, PageDO pageDO) {
        PageHelper.startPage(pageDO.getPageNum(), pageDO.getPageSize());
        return BatchResultDTO.of(mapper.select(obj));
    }


}
