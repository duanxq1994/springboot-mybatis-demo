package com.xinge.demo.core.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author duanxq
 * created by duanxq on 2019/6/11
 */
public class PasswordHelperTest {

    @Test
    public void encryptPassword() {
        String s = new SimpleHash(PasswordHelper.ALGORITHM_NAME, "admin", "RaGapN", PasswordHelper.HASH_ITERATIONS).toHex();
        String s1 = new SimpleHash(PasswordHelper.ALGORITHM_NAME, "admin", ByteSource.Util.bytes("RaGapN"), PasswordHelper.HASH_ITERATIONS).toHex();
        System.out.println(s);
        System.out.println(s1);
        Assert.assertEquals("ee288c8cb70eaa37f670ba077384b5e4", s);
        Assert.assertEquals("ee288c8cb70eaa37f670ba077384b5e4", s1);
    }
}