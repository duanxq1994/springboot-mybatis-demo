package com.xinge.demo.core.retry.model;

import com.xinge.demo.common.util.SpringBeanUtil;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author BG343674
 * created by BG343674 on 2019/9/20
 */
@Data
public class RetryTargetInfo implements Serializable {

    private static final long serialVersionUID = -6025539471050053991L;

    private Class<?> clazz;

    private String method;

    private Class<?>[] argsType;

    private Object[] args;

    public Object invoke() throws Exception {
        Object bean = SpringBeanUtil.getBean(clazz);
        Method method = clazz.getMethod(this.method, argsType);
        return method.invoke(bean, args);
    }

}
