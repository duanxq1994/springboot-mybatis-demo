/**
 * 版权所有：深圳法大大网络科技有限公司
 * Copyright 2015 fadada.com Inc.
 * All right reserved.
 * ====================================================
 * 文件名称: FddHttpUtil.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    Dec 18, 2015			Mocuishle(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package com.xinge.demo.common.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class FddClientUtil {

    public static final String charset = "UTF-8";

    /*===================================doPost==============================================*/

    /**
     * <b>概要：</b>
     * post请求方法
     * <b>作者：</b>zhouxw </br>
     * <b>日期：</b>2015年12月17日 </br>
     *
     * @param url
     * @param params
     * @return 链接响应内容
     */
    public static String doPost(String url, List<NameValuePair> params) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            if (null != params && params.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * <b>概要：</b>请求地址返回文件流
     * <b>作者：</b>zhouxw </br>
     * <b>日期：</b>2016年7月4日 </br>
     *
     * @param path   保存地址
     * @param url
     * @param params
     */
    public static void doPostDownload(String path, String url, List<NameValuePair> params) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            // 设置参数
            if (null != params && params.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response == null) {
                System.out.println("#doPostDownload 地址无响应.");
                return;
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                byte[] result = EntityUtils.toByteArray(response.getEntity());
                BufferedOutputStream bw = null;
                try {
                    File f = new File(path); // 创建文件对象
                    if (f.exists()) { // 重复时候替换掉
                        f.delete();
                    }
                    if (!f.getParentFile().exists()) { // 创建文件路径
                        f.getParentFile().mkdirs();
                    }
                    bw = new BufferedOutputStream(new FileOutputStream(path));// 写入文件
                    bw.write(result);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else { // 如果失败，打印返回头信息查错
                StringBuffer errorMsg = new StringBuffer();
                errorMsg.append("httpStatus:");
                errorMsg.append(response.getStatusLine().getStatusCode());
                errorMsg.append(response.getStatusLine().getReasonPhrase());
                errorMsg.append(", Header: ");
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    errorMsg.append(header.getName());
                    errorMsg.append(":");
                    errorMsg.append(header.getValue());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*===================================doPost==============================================*/

    /**
     * <b>概要：</b>
     * 获取当前时间戳
     * <b>作者：</b>zhouxw </br>
     * <b>日期：</b>2015年12月17日 </br>
     *
     * @return 当前时间：'yyyyMMddHHmmss'格式
     */
    public static String getTimeStamp() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(ts);
    }
}
