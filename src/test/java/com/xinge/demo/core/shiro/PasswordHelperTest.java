package com.xinge.demo.core.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author BG343674
 * created by BG343674 on 2019/6/11
 */
public class PasswordHelperTest {

    @Test
    public void encryptPassword() {
        String s = new SimpleHash(PasswordHelper.ALGORITHM_NAME, "admin", "RaGapN", PasswordHelper.HASH_ITERATIONS).toHex();
        System.out.println(s);
        Assert.assertEquals("ee288c8cb70eaa37f670ba077384b5e4", s);
    }
}