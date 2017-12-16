package com.xinge.demo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登陆注解
 * created by duanxq on 2017/4/17
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
    /**
     * 是否需要登陆 默认true
     * @return value
     */
    boolean value() default true;
}
