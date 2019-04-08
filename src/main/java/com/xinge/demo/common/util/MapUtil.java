package com.xinge.demo.common.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duanxq on 2016/11/14.
 */
@Slf4j
public class MapUtil {

    private MapUtil() {

    }

    /**
     * 将javabean实体类转为map类型，然后返回一个map类型的值
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!"class".equals(key)) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    params.put(key, value);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return params;
    }

    /**
     * 将map转为javaBean
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz) {
        T bean = null;
        try {
            //处理时间格式
            DateConverter dateConverter = new DateConverter();
            //设置日期格式
            dateConverter.setPatterns(DateUtil.DEFAULT_FORMATS);
            //注册格式
            ConvertUtils.register(dateConverter, Date.class);
            bean = clazz.newInstance();
            BeanUtils.populate(bean, map);
        } catch (Exception e) {
            log.error("", e);
        }
        return bean;
    }

}
