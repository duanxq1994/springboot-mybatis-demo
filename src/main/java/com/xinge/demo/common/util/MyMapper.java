package com.xinge.demo.common.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * created by duanxq on 2017/4/13
 *
 * @param <T>
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    //特别注意，该接口不能被扫描到，否则会出错
}
