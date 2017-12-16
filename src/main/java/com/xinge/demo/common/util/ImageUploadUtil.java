package com.xinge.demo.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinge.demo.core.exception.BizException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 上传文件到静态服务器
 * created by duanxq on 2017/4/27
 */
public class ImageUploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadUtil.class);

    private final static String imageRemotePostUrl = "http://img.hpbanking.com/file/uploading";


    /**
     * 上传文件到静态服务器
     *
     * @param imageBytes
     * @param uploadPath
     * @return ResultEntity<br>
     */
    public static String saveAsRemoteWithResult(byte[] imageBytes, String uploadPath, Integer cutWide, Integer cutHigh) {
        logger.info("开始传送文件 Server:" + imageRemotePostUrl + ", uploadPath:" + uploadPath);
        if (imageBytes == null) {
            throw new IllegalArgumentException("请上传图片");
        }
        // 压缩图片并转成JPEG格式
        BufferedImage bufferedImage;
        String MD5CheckCode = null;
        try {
            bufferedImage = ImageCompressUtil.zoomImage(new ByteArrayInputStream(imageBytes));
            if (cutWide != null && cutHigh != null) {
                imageBytes = CutImage.cutImageService(bufferedImage, cutWide, cutHigh);
            }
            byte[] zoomImageData = ImageCompressUtil.getJPEGImageBytes(bufferedImage);
            // 生成md5校验码
            MD5CheckCode = MD5Util.getMD5String(zoomImageData);
            // 组装请求报文
            HttpPost httpPost = new HttpPost(imageRemotePostUrl);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("type", uploadPath));
            // 添加文件列表
            Map<String, ByteArrayBody> files = new HashMap<>();
            //            files.put("file", new ByteArrayBody(imageBytes, MD5CheckCode + "_" + cutWide + "x" + cutHigh + ".jpg"));
            files.put("file", new ByteArrayBody(imageBytes, MD5CheckCode + ".jpg"));
            httpPost.setEntity(makeMultipartEntity(params, files));
            CloseableHttpClient client = HttpClientBuilder.create().build();
            // 执行上传请求
            CloseableHttpResponse response = client.execute(httpPost);
            String html = "";
            Scanner sc = new Scanner(response.getEntity().getContent());
            while (sc.hasNextLine()) {
                html += sc.nextLine() + "\r\n";
            }
            sc.close();
            logger.info("静态服务器返回信息：" + html);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(html);
            int code = jsonNode.findValue("code").asInt();
            String msg = jsonNode.findValue("msg").asText();
            if (code == 0) {
                return jsonNode.findValue("url").asText();
            } else {
                throw new RuntimeException(msg);
            }
        } catch (Exception e) {
            logger.error("文件上传失败：", e);
            throw new BizException("文件上传失败，请检查文件是否符合要求");
        } finally {
            logger.info("传送文件 Server:" + imageRemotePostUrl + ", uploadPath:" + uploadPath + "结束");
        }
    }

    private static HttpEntity makeMultipartEntity(List<NameValuePair> params, Map<String, ByteArrayBody> files) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(StandardCharsets.UTF_8);
        // 不要用这个，会导致服务端接收不到参数
        if (params != null && params.size() > 0) {
            for (NameValuePair p : params) {
                builder.addTextBody(p.getName(), p.getValue(), ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8));
            }
        }
        if (files != null && files.size() > 0) {
            Set<Map.Entry<String, ByteArrayBody>> entries = files.entrySet();
            for (Map.Entry<String, ByteArrayBody> entry : entries) {
                builder.addPart(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

}
