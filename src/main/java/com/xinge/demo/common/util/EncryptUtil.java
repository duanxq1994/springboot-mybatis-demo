package com.xinge.demo.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 加解密工具
 */
public class EncryptUtil {

    private static final String STR_DES_EDE = "DESede";
    private static final String ENCRYPT_TYPE_STR = "AES/GCM/NoPadding";

    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};
    private static final byte[] IV = {0x12, 0x34, 0x56, 0x78, -0x12, -0x34, -0x56, -0x78, 0x12, 0x34, 0x56,
            0x78, -0x56, -0x78, 0x12, 0x34};

    /**
     * 加密字段内容
     *
     * @param key   所属字段名称
     * @param value 需要加密的内容
     * @return 加密后的字符串
     */
    public static String encryptValue(String key, String value) {
        try {
            Cipher aesCipher = Cipher.getInstance(ENCRYPT_TYPE_STR);
            String sKey = key + "eid";
            byte[] raw = new byte[16];
            System.arraycopy(sKey.getBytes("ASCII"), 0, raw, 0, sKey.length() > 16 ? 16 : sKey.length());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec ips = new IvParameterSpec(IV);
            aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
            byte[] encryptRaw = aesCipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.encodeBase64(encryptRaw));
        } catch (Exception e) {
            logger.error("AES加密失败", e);
        }
        return "";
    }


    /**
     * 解密字段内容
     *
     * @param key   所属字段名称
     * @param value 需要解密的内容
     * @return 解密后的原文
     */
    public static String decryptValue(String key, String value) {
        try {
            Cipher aesCipher = Cipher.getInstance(ENCRYPT_TYPE_STR);
            String sKey = key + "eid";
            byte[] raw = new byte[16];
            System.arraycopy(sKey.getBytes("ASCII"), 0, raw, 0, sKey.length() > 16 ? 16 : sKey.length());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec ips = new IvParameterSpec(IV);
            aesCipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
            byte[] decryptRaw = aesCipher.doFinal(Base64.decodeBase64(value.getBytes()));
            return new String(decryptRaw, "utf-8");
        } catch (Exception e) {
            logger.error("AES解密失败", e);
        }
        return "";
    }

    public static synchronized String serializeClassToBase64(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.flush();
        byte[] objectData = bos.toByteArray();
        oos.close();
        return new String(Base64.encodeBase64(objectData));
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> T deserializeBase64ToObject(String base64String) throws IOException, ClassNotFoundException {
        byte[] objectData = Base64.decodeBase64(base64String.getBytes());
        ByteArrayInputStream bis = new ByteArrayInputStream(objectData);
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {

            return (T) ois.readObject();
        }
    }

    /**
     * 计算指定字符串的MD5值
     *
     * @param value
     * @return
     */
    public static String encodeMd5(String value) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(value.getBytes("utf-8"));
            byte[] encodedRaw = md5.digest();
            int j = encodedRaw.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : encodedRaw) {
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("MD5加密失败", e);
        }
        return "";
    }

    /**
     * 计算指定字节数组的MD5值
     *
     * @param bytes
     * @return
     */
    public static String encodeMd5(byte[] bytes) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(bytes);
            byte[] encodedRaw = md5.digest();
            int j = encodedRaw.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : encodedRaw) {
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("MD5加密失败", e);
        }
        return "";
    }

    /**
     * 3DESECB加密,key必须是长度大于等于 3*8 = 24 位哈
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public String encryptThreeDESECB(final String src, final String key) {
        try {
            final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(STR_DES_EDE);
            final SecretKey securekey = keyFactory.generateSecret(dks);

            final Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_STR);
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            final byte[] b = cipher.doFinal(src.getBytes());
            return Base64.encodeBase64String(b).replaceAll("\r", "").replaceAll("\n", "");
        } catch (Exception e) {
            logger.warn("加密失败：", e);
        }
        return null;

    }

    /**
     * 3DESECB解密,key必须是长度大于等于 3*8 = 24 位哈
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] decryptThreeDESECB(final String src, final String key) {
        try {
            // --通过base64,将字符串转成byte数组
            final byte[] bytesrc = Base64.decodeBase64(src);
            // --解密的key
            final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes());
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(STR_DES_EDE);
            final SecretKey securekey = keyFactory.generateSecret(dks);
            // --Chipher对象解密
            final Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_STR);
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            return cipher.doFinal(bytesrc);
        } catch (Exception e) {
            logger.error("解密失败：", e);
        }
        return new byte[0];
    }


    /**
     * 加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    public static byte[] encrypt3DES(byte[] datasource, String password) {
        try {
            String algorithm = STR_DES_EDE;
            byte[] bytePassword = password.getBytes();
            //生成密钥
            int keyLength = 24;
            byte[] tripleDESKey = new byte[keyLength];
            //初始化Key
            for (int i = 0; i < bytePassword.length; i++) {
                if (i >= keyLength) {
                    break;
                }
                tripleDESKey[i] = bytePassword[i];
            }
            SecretKey deskey = new SecretKeySpec(tripleDESKey, algorithm);

            //加密
            Cipher c1 = Cipher.getInstance(ENCRYPT_TYPE_STR);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(datasource);
        } catch (Exception e) {
            logger.warn("", e);
            throw new IllegalArgumentException(e);
        }
    }


    public static String decrypt3DES(byte[] datasource, String password) {
        try {
            String algorithm = STR_DES_EDE;
            byte[] bytePassword = password.getBytes();
            //生成密钥
            int keyLength = 24;
            byte[] tripleDESKey = new byte[24];
            //初始化Key
            for (int i = 0; i < bytePassword.length; i++) {
                if (i >= keyLength) {
                    break;
                }
                tripleDESKey[i] = bytePassword[i];
            }
            SecretKey deskey = new SecretKeySpec(tripleDESKey, algorithm);
            Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE_STR);
            cipher.init(Cipher.DECRYPT_MODE, deskey);
            byte[] retByte = cipher.doFinal(datasource);
            return new String(retByte);
        } catch (Exception e) {
            logger.warn("", e);
            throw new IllegalArgumentException(e);
        }
    }
}
