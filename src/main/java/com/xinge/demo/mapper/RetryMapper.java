package com.xinge.demo.mapper;

import com.xinge.demo.common.util.MyMapper;
import com.xinge.demo.model.Retry;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author duanxq
 * Created by duanxq on 2019-09-20.
 */
@Repository
public interface RetryMapper extends MyMapper<Retry> {

    @Select("select * from retry where status in (1, 3)")
    List<Retry> selectTask();

}
