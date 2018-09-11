package com.xinge.demo.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xinge.demo.common.util.MyMapper;
import com.xinge.demo.model.entity.BatchResultDTO;
import com.xinge.demo.model.entity.PageDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 *
 * @author duanxq
 * @date 2017/9/9
 */
public abstract class BaseService<T> {

    @Autowired
    public MyMapper<T> mapper;

    protected Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 新增
     *
     * @param obj
     * @return
     */
    public void add(T obj) {
        mapper.insertSelective(obj);
    }


    /**
     * 删除
     *
     * @param key
     * @return
     */
    public void del(Object key) {
        mapper.deleteByPrimaryKey(key);
    }

    /**
     * 根据主键编辑
     *
     * @param obj
     * @return
     */
    public void editByPK(T obj) {
        mapper.updateByPrimaryKeySelective(obj);
    }

    /**
     * 根据条件编辑
     *
     * @param obj     需要编辑的内容
     * @param example 编辑条件
     */
    public void editByExample(T obj, Example example) {
        mapper.updateByExampleSelective(obj, example);
    }

    /**
     * 根据主键查询
     *
     * @param key
     * @return
     */
    public T queryByPK(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    /**
     * 根据条件查询list
     *
     * @param obj
     * @return
     */
    public List<T> queryForList(T obj) {
        return mapper.select(obj);
    }

    /**
     * 根据条件count
     * @param obj
     * @return
     */
    public Integer queryForCount(T obj) {
        return mapper.selectCount(obj);
    }

    /**
     * 分页查询list
     *
     * @param obj
     * @return
     */
    public BatchResultDTO<T> queryForPageList(T obj) {
        Assert.isTrue(obj instanceof PageDO, String.format("%s need to extends pageDo", obj.getClass().getName()));
        BatchResultDTO<T> resultDTO = new BatchResultDTO<>();
        PageHelper.startPage(((PageDO)obj).getPageNum(), ((PageDO)obj).getPageSize());
        Page<T> page = (Page<T>) mapper.select(obj);
        resultDTO.setModule(page.getResult());
        resultDTO.setCount(page.getTotal());
        return resultDTO;
    }


}
