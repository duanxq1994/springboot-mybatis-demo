package com.xinge.demo.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 功能说明: HttpClient工具类；支持http/https、单向/双向认证；默认执行后自动关闭；
 *
 * @author duanxq
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static HttpClientUtil instance;
    private HttpClientBuilder httpClientBuilder;

    private Charset charset = StandardCharsets.UTF_8;
    private ContentType contentType = ContentType.APPLICATION_FORM_URLENCODED.withCharset(charset);
    /**
     * 超时时间
     */
    private int timeout = 5 * 1000;
    /**
     * 代理地址
     */
    private HttpHost proxyHost;

    /**
     * 构造器
     */
    private HttpClientUtil() {
    }


    public static HttpClientUtil getInstance() {
        if (instance == null) {
            synchronized (HttpClientUtil.class) {
                if (instance == null) {
                    // 设置连接管理器
                    PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
                    connManager.setMaxTotal(200);
                    connManager.setDefaultMaxPerRoute(20);
                    IdleConnectionMonitorThread idleConnectionMonitorThread = new IdleConnectionMonitorThread(connManager);
                    idleConnectionMonitorThread.start();
                    instance = new HttpClientUtil();
                    instance.httpClientBuilder = HttpClients.custom().setConnectionManager(connManager);
                }
            }
        }
        return instance;
    }

    /**
     * 指定编码字符集
     *
     * @param charset
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
        this.contentType = this.contentType.withCharset(charset);
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 判断是否为Multipart请求
     *
     * @param params
     * @return
     */
    private boolean isMultipart(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return false;
        }
        if (StringUtils.contains(this.contentType.toString(), "multipart/form-data")) {
            return true;
        }
        for (Entry<String, Object> entry : params.entrySet()) {
            Object pvalue = entry.getValue();
            if (pvalue == null) {
                continue;
            }
            if (pvalue instanceof File || pvalue instanceof InputStreamBody || pvalue instanceof ByteArrayBody) {
                return true;
            } else if (pvalue instanceof List) {
                for (Object obj : (List) pvalue) {
                    if (obj instanceof File || obj instanceof InputStreamBody || obj instanceof ByteArrayBody) {
                        return true;
                    }
                }
            } else if (pvalue.getClass().isArray()) {
                for (Object obj : (Object[]) pvalue) {
                    if (obj instanceof File || obj instanceof InputStreamBody || obj instanceof ByteArrayBody) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 构建http请求实体，支持字符串及文件
     *
     * @param params
     * @return
     */
    private HttpEntity buildMultipartHttpEntry(Map<String, Object> params) {

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(charset);

        if (params == null || params.size() == 0) {
            return builder.build();
        }

        for (Entry<String, Object> entry : params.entrySet()) {

            String pname = entry.getKey();
            Object pvalue = entry.getValue();

            // 如果pvalue为null，就可以不放到http请求的参数中。这样接请求接收方获取到的是null对象，避免接收请求方得到“null”字符串
            if (pvalue == null) {
                continue;
            }

            if (pvalue instanceof String) {
                builder.addTextBody(pname, String.valueOf(pvalue), contentType);
                continue;
            }

            if (pvalue instanceof File) {
                builder.addBinaryBody(pname, (File) pvalue);
                continue;
            }

            if (pvalue instanceof MultipartFile) {
                MultipartFile multipartFile = (MultipartFile) pvalue;
                try {
                    builder.addPart(pname, new InputStreamBody(multipartFile.getInputStream(), contentType, multipartFile.getOriginalFilename()));
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
                continue;
            }

            if (pvalue instanceof ByteArrayBody) {
                builder.addPart(pname, (ByteArrayBody) pvalue);
                continue;
            }

            if (pvalue instanceof InputStreamBody) {
                builder.addPart(pname, (InputStreamBody) pvalue);
                continue;
            }

            if (pvalue instanceof List) {
                for (Object obj : (List) pvalue) {
                    builder.addTextBody(pname, String.valueOf(obj), contentType);
                }
                continue;
            }

            if (pvalue.getClass().isArray()) {
                Object[] array = (Object[]) pvalue;
                for (Object anArray : array) {
                    builder.addTextBody(pname, String.valueOf(anArray), contentType);
                }
                continue;
            }

            // 兼容处理，long、int等基本类型
            builder.addTextBody(pname, String.valueOf(pvalue), contentType);
        }

        return builder.build();
    }

    /**
     * POST方式请求，支持http/https，支持提交字符串/文件
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public byte[] httpPost(String url, Map<String, Object> params, List<Header> headers) throws IOException {
        HttpPost httppost = new HttpPost(url);
        if (isMultipart(params)) {
            HttpEntity reqEntity = buildMultipartHttpEntry(params);
            httppost.setEntity(reqEntity);
        } else {
            HttpEntity reqEntity = buildHttpEntry(params);
            httppost.setEntity(reqEntity);
        }
        return execute(httppost, headers);
    }

    /**
     * POST方式请求，支持http/https，支持提交xml
     *
     * @param url
     * @param xml
     * @param headers
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public byte[] httpPost(String url, String xml, List<Header> headers) throws IOException {
        HttpPost httppost = new HttpPost(url);
        if (StringUtils.contains(this.contentType.toString(), "x-www-form-urlencoded")) {
            this.setContentType(ContentType.APPLICATION_XML.withCharset(charset));
        }
        HttpEntity reqEntity = buildHttpEntry(xml);
        httppost.setEntity(reqEntity);
        return execute(httppost, headers);
    }

    /**
     * POST方式请求，支持http/https，支持提交xml
     *
     * @param url
     * @param xml
     * @param headers
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public byte[] httpPost(String url, String xml, ContentType content_type, List<Header> headers) throws IOException {
        HttpPost httppost = new HttpPost(url);
        this.setContentType(content_type.withCharset(charset));
        HttpEntity reqEntity = buildHttpEntry(xml);
        httppost.setEntity(reqEntity);
        return execute(httppost, headers);
    }


    /**
     * 构造普通http请求实体
     *
     * @param obj
     * @return
     */
    private HttpEntity buildHttpEntry(Object obj) {
        EntityBuilder builder = EntityBuilder.create();
        builder.setContentEncoding(this.charset.name());
        builder.setContentType(this.contentType);
        if (obj instanceof File) {
            builder.setFile((File) obj);
        } else if (obj instanceof InputStream) {
            builder.setStream((InputStream) obj);
        } else if (obj instanceof byte[]) {
            builder.setBinary((byte[]) obj);
        } else if (obj instanceof Map) {
            List<NameValuePair> list = new ArrayList<>();
            for (Entry<String, Object> entry : ((Map<String, Object>) obj).entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (entry.getValue() instanceof List) {
                    for (Object val : (List) entry.getValue()) {
                        list.add(new BasicNameValuePair(entry.getKey(), (String) val));
                    }
                } else if (entry.getValue().getClass().isArray()) {
                    for (Object val : (Object[]) entry.getValue()) {
                        list.add(new BasicNameValuePair(entry.getKey(), (String) val));
                    }
                } else {
                    list.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
            }
            builder.setParameters(list);
        } else {
            builder.setText((String) obj);
        }
        return builder.build();
    }

    /**
     * POST方式请求，支持http/https，支持提交bytes类型
     *
     * @param url
     * @param binary
     * @param headers
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public byte[] httpPost(String url, byte[] binary, List<Header> headers) throws ClientProtocolException, IOException {
        HttpPost httppost = new HttpPost(url);
        this.setContentType(ContentType.APPLICATION_OCTET_STREAM.withCharset(charset));
        HttpEntity reqEntity = buildHttpEntry(binary);
        httppost.setEntity(reqEntity);
        return execute(httppost, headers);
    }

    /**
     * GET方式请求，支持http/https
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public byte[] httpGet(String url, List<Header> headers) throws IOException {
        HttpGet httpget = new HttpGet(url);
        return execute(httpget, headers);
    }

    /**
     * Post方式请求，支持http/https
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public byte[] httpPost(String url, List<Header> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        return execute(httpPost, headers);
    }

    /**
     * 执行http请求，解析输出流为字节
     *
     * @param request
     * @param headers 不能为null，否则不能把执行post请求得到的header放进去
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    private byte[] execute(HttpRequestBase request, List<Header> headers) throws IOException {
        if (headers != null && headers.size() > 0) {
            // 添加请求头
            for (Header header : headers) {
                request.addHeader(header);
            }
        } else if (headers == null) {
            // 如果入参的headers为null,后面headers.add(header)是返回不出去的。这一句只是为了避免空指针异常
            headers = new ArrayList<>();
        }
        CloseableHttpClient httpclient;
        CloseableHttpResponse response = null;
        try {
            if (proxyHost != null) {
                httpclient = httpClientBuilder.setProxy(proxyHost).build();
            } else {
                httpclient = httpClientBuilder.build();
            }
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
            request.setConfig(requestConfig);
            response = httpclient.execute(request);
            for (Header header : response.getAllHeaders()) {
                if ("Set-Cookie".equalsIgnoreCase(header.getName())) {
                    // 回写返回的cookie
                    headers.add(header);
                }
            }
            HttpEntity entity = response.getEntity();
            byte[] data = EntityUtils.toByteArray(entity);
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
                return data;
            } else {
                String dataStr = data == null ? "网络请求发生异常:" + response.getStatusLine() + " URI=" + request.getURI() : new String(data, StandardCharsets.UTF_8);
                logger.warn("网络请求发生异常:" + dataStr);
                throw new RuntimeException("网络请求发生异常:" + response.getStatusLine());
            }
        } finally {
            if (request != null) {
                try {
                    request.completed();
                    request.abort();
                } catch (Exception e) {
                    logger.warn("关闭request发生错误", e);
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    logger.warn("关闭response发生错误", e);
                }
            }
        }
    }

    public void setProxyHost(HttpHost proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyHost(String hostname, int port) {
        this.proxyHost = new HttpHost(hostname, port);
    }

}
