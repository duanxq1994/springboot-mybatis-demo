package com.xinge.demo.common.util;

import java.util.Date;
import java.util.Random;

/**
 * @author zhangsheng
 * @version $Id: SerialNumberUtil.java, v 0.1 2016年5月24日 上午11:37:14 zs $
 */
public class SerialNumberUtil {
    /**
     * 自定义进制(0,1没有加入,容易与o,l混淆)
     */
    private static final char[] r = new char[]{'Q', 'w', 'E', '8', 'a', 'S', '2', 'd', 'Z', 'x', '9', 'c', '7', 'p', 'O', '5',
            'i', 'K', '3', 'm', 'j', 'U', 'f', 'r', '4', 'V', 'y', 'L', 't', 'N', '6', 'b', 'g', 'H'};
    /**
     * 自动补全组(不能与自定义进制有重复)
     */
    private static final char[] b = new char[]{'q', 'W', 'e', 'A', 's', 'D', 'z', 'X', 'C', 'P', 'o', 'I', 'k', 'M', 'J', 'u',
            'F', 'R', 'v', 'Y', 'T', 'n', 'B', 'G', 'h'};
    /**
     * 进制长度
     */
    private static final int l = r.length;
    /**
     * 序列最小长度
     */
    private static final int s = 6;

    /*******************另一种随机方式**********************************/
    public static final long MIN_VALUE = 0x8000000000000000L;

    public static final long MAX_VALUE = 0x7fffffffffffffffL;

    final static char[] digits = {'0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '-', '_'};

    /**
     * 根据ID生成六位随机码
     *
     * @param num ID
     * @return 随机码
     */
    public static String toSerialNumber() {
        long num = (int) ((Math.random() * 9 + 1) * 10000);
        char[] buf = new char[32];
        int charPos = 32;

        while ((num / l) > 0) {
            buf[--charPos] = r[(int) (num % l)];
            num /= l;
        }
        buf[--charPos] = r[(int) (num % l)];
        String str = new String(buf, charPos, (32 - charPos));
        //不够长度的自动随机补全
        if (str.length() < s) {
            StringBuffer sb = new StringBuffer();
            Random rnd = new Random();
            for (int i = 0; i < s - str.length(); i++) {
                sb.append(b[rnd.nextInt(24)]);
            }
            str += sb.toString();
        }
        return str;
    }

    /*******************另一种随机方式**********************************/
    private static String toUnsignedString(long i, int shift) {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = radix - 1;
        do {
            buf[--charPos] = digits[(int) (i & mask)];
            i >>>= shift;
        } while (i != 0);
        return new String(buf, charPos, (64 - charPos));
    }


    // j为2的次方，如转成16进制就是4，32进制就是5...
    public static String getRand(long i, int j) {
        return toUnsignedString(i, j);
    }

    // 随机码＋时间戳＋随机码的生成
    public static Long getRand() {
        String str1, str2, str3;
        str1 = getRandStr(2);
        str3 = getRandStr(3);
        str2 = (new Date()).getTime() + "";
        //System.out.println(str1+str2+str3);
        return Long.parseLong(str1 + str2 + str3);
    }

    // 主键生成
    public static String getKey() {
        return getRand(getRand(), 6);
    }

    //    生成指定长度的随机串
    public static String getRandStr(Integer length) {
        String str = "";
        while (str.length() != length) {
            str = (Math.random() + "").substring(2, 2 + length);
        }
        return str;
    }


    public static void main(String[] args) {
        String a = "";
        try {
            while (true) {
                a = toSerialNumber();
                System.out.println(a);
                Thread.sleep(1l);
            }
        } catch (Exception e) {

        }
//        Integer b = 0;
//        while(true) {
//            b = (int)((Math.random()*9+1)*100000);
//            System.out.println(b);
//        }
//        for(int i=0;i<10;i++){
//            System.out.println("第"+i+"个："+getKey());
//       }

    }

}
