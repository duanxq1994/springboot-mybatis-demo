package com.xinge.demo.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 日志打印前过滤敏感信息
 * @author duanxq
 * @date 2018/01/23
 */
public class DebugHelper {

    private DebugHelper() {

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map filterField(final Map map) {
        if (map == null) {
            return null;
        }
        Map filteredMap = new HashMap(map);

        // 如果map中有字符串数组，需要自己拼接成字符串
        java.util.Set keySet = filteredMap.keySet();
        for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
            Object key = iterator.next();
            Object value = filteredMap.get(key);
            if (value != null && value.getClass().isArray()) {
                Object[] array = (Object[]) value;
                // 逗号后面加个空格，和list打印的格式效果保持一致
                filteredMap.put(key, "[" + StringUtils.join(array, ", ") + "]");
            }
        }

        // 密码
        if (filteredMap.get("tradePwd") != null) {
            filteredMap.put("tradePwd", replacePassword(String.valueOf(filteredMap.get("tradePwd"))));
        }
        if (filteredMap.get("newTradePwd") != null) {
            filteredMap.put("newTradePwd", replacePassword(String.valueOf(filteredMap.get("newTradePwd"))));
        }

        // 身份证
        if (filteredMap.get("id_no") != null) {
            filteredMap.put("id_no", encryptIdNo(String.valueOf(filteredMap.get("id_no"))));
        }
        if (filteredMap.get("identify_code") != null) {
            filteredMap.put("identify_code", encryptIdNo(String.valueOf(filteredMap.get("identify_code"))));
        }

        // 手机号
        if (filteredMap.get("mobile") != null) {
            filteredMap.put("mobile", encryptMobileTel(String.valueOf(filteredMap.get("mobile"))));
        }

        // 银行卡号
        if (filteredMap.get("cardNo") != null) {
            // bank_account可能传一个ArrayList，直接使用String强转会报错
            Object obj = filteredMap.get("cardNo");
            StringBuilder encryptedBankNo = new StringBuilder();
            if (obj instanceof List) {
                List tmpList = (List) obj;
                for (Object aTmpList : tmpList) {
                    encryptedBankNo.append(encryptBankAccount(String.valueOf(aTmpList))).append(",");
                }
                if (encryptedBankNo.toString().endsWith(",")) {
                    encryptedBankNo = new StringBuilder(encryptedBankNo.substring(0, encryptedBankNo.length() - 1));
                }
                encryptedBankNo = new StringBuilder("[" + encryptedBankNo + "]");
            } else {
                encryptedBankNo = new StringBuilder(encryptBankAccount(String.valueOf(obj)));
            }
            filteredMap.put("cardNo", encryptedBankNo.toString());
        }

        // 地址
        if (filteredMap.get("address") != null) {
            filteredMap.put("address", encryptAddress(String.valueOf(filteredMap.get("address"))));
        }
        if (filteredMap.get("id_address") != null) {
            filteredMap.put("id_address", encryptAddress(String.valueOf(filteredMap.get("id_address"))));
        }

        return filteredMap;
    }

    /**
     * 将密码替换成6个星号
     *
     * @param password
     * @return
     * @author yejg
     */
    public static String replacePassword(String password) {
        if (password == null || password.length() == 0) {
            return password;
        }
        return "******";
    }

    /**
     * 加密身份证号
     *
     * @param idNo
     * @return
     */
    public static String encryptIdNo(String idNo) {
        return replace(idNo, '*', 6, 2);
    }

    /**
     * 加密手机号码
     *
     * @param mobileTel
     * @return
     */
    public static String encryptMobileTel(String mobileTel) {
        return replace(mobileTel, '*', 3, 4);
    }

    /**
     * 加密地址
     *
     * @param address
     * @return
     */
    public static String encryptAddress(String address) {
        return replace(address, '*', 3, 4);
    }

    /**
     * 加密姓名
     *
     * @param clientName
     * @return
     */
    public static String encryptClientName(String clientName) {
        return replace(clientName, '*', 0, 1);
    }

    /**
     * 加密银行账号
     *
     * @param bankAccount
     * @return
     */
    public static String encryptBankAccount(String bankAccount) {
        return replace(bankAccount, '*', 4, 4);
    }

    /**
     * 敏感字符串"*"号处理
     *
     * @param string      待处理的字符串
     * @param repChar     被替换的字符
     * @param totalPrefix 前缀保留原始字符数量
     * @param totalSuffix 后缀保留原始字符数量
     * @return 处理结果字符串
     */
    public static String replace(String string, char repChar, int totalPrefix, int totalSuffix) {
        if (string == null || string.length() == 0) {
            return string;
        }
        if (totalPrefix >= string.length() || totalSuffix >= string.length()) {
            return string;
        } else {
            char[] chr = new char[string.length()];
            for (int i = 0; i < string.length(); i++) {
                chr[i] = repChar;
            }
            if (totalPrefix != 0 && totalPrefix <= string.length()) {
                for (int i = 0; i < totalPrefix; i++) {
                    chr[i] = string.charAt(i);
                }
            }
            if (totalSuffix != 0 && totalSuffix <= string.length()) {
                for (int i = string.length(); (string.length() - i) < totalSuffix; i--) {
                    chr[i - 1] = string.charAt(i - 1);
                }
            }
            StringBuilder sb = new StringBuilder();
            for (char aChr : chr) {
                sb.append(aChr);
            }
            return sb.toString();
        }
    }

}
