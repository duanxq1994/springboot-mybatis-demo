package com.xinge.demo.common.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author duanxq
 * @date 2018/1/23
 */
public class IDCardValidateTest {

    @Test
    public void validate() {
        // 正确
        Assert.assertTrue(IDCardValidate.validate("432831196411150810"));
        // 错误
        Assert.assertTrue(!IDCardValidate.validate("432831196411150811"));
        Assert.assertTrue(!IDCardValidate.validate("432831196411150813"));
    }
}