package com.xinge.demo.common.util;

/**
 * @author BG343674
 * created by BG343674 on 2019/11/23
 */
public interface ObjectSerializer<T> {

    byte[] serialize(T t);

    T deserialize(byte[] bytes);


}
