package com.xinge.demo.common.util;


import org.junit.Assert;
import org.junit.Test;

/**
 * @author duanxq
 * @date 2018/1/23
 */
public class Md5UtilTest {

    @Test
    public void getMD5String() {
        Assert.assertEquals("e10adc3949ba59abbe56e057f20f883e", Md5Util.getMD5String("123456"));
        Assert.assertEquals("fcea920f7412b5da7be0cf42b8c93759", Md5Util.getMD5String("1234567"));
    }

}