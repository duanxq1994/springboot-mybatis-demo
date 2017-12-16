package com.xinge.demo.web.init;

import com.xinge.demo.mapper.AreaMapper;
import com.xinge.demo.model.domain.AreaDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 省市区缓存
 *
 * @author duanx
 * @date 2017/9/14
 */
@Component
public class AreaCache implements CommandLineRunner {

    private static Map<String, AreaDO> cache = new HashMap<>(4096);
    @Autowired
    private AreaMapper areaMapper;
    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void run(String... args) {
        try {
            //共3659条记录，占内存约1288528字节
            for (AreaDO areaDO : areaMapper.selectAll()) {
                cache.put(areaDO.getAreaCode(), areaDO);
            }
            logger.info("省市区缓存初始化成功{}", cache.size());
        } catch (Exception e) {
            logger.warn("省市区缓存初始化失败");
        }
    }


    private static AreaDO getAreaByCode(String areaCode) {
        return cache.get(areaCode);
    }

    public static String getAreaNameByAreaCode(String areaCode) {
        AreaDO areaByCode = getAreaByCode(areaCode);
        return areaByCode == null ? "" : areaByCode.getAreaName();
    }

}
