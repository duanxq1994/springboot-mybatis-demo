package com.xinge.demo.core.shiro;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @author duanxq
 * created by duanxq on 2019/3/11
 */
public class PasswordHelper {

    /**
     * 基础散列算法
     */
    public static final String ALGORITHM_NAME = Md5Hash.ALGORITHM_NAME;
    /**
     * 自定义散列次数
     */
    public static final int HASH_ITERATIONS = 1;

    public static String encryptPassword(String password) {
        // 随机字符串作为salt因子
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = randomNumberGenerator.nextBytes().toHex();
        return new SimpleHash(ALGORITHM_NAME, password, salt, HASH_ITERATIONS).toHex();
    }

}
