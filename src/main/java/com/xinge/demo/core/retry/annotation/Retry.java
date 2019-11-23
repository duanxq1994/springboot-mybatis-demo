package com.xinge.demo.core.retry.annotation;

import java.lang.annotation.*;

/**
 * @author BG343674
 * created by BG343674 on 2019/9/19
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    /**
     * 重试异常的错误信息
     * @return
     */
    String value() default "重试异常";

}
