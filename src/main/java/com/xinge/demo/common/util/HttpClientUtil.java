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
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 功能说明: HttpClient工具类；支持http/https、单向/双向认证；默认执行后自动关闭；
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private Charset charset = StandardCharsets.UTF_8;
    private ContentType content_type = ContentType.APPLICATION_FORM_URLENCODED.withCharset(charset);

    private int timeout = 60 * 1000; // 60秒

    private PoolingHttpClientConnectionManager connManager;
    private HttpHost proxyHost;// 代理地址

    private static Map<String, Registry<ConnectionSocketFactory>> registryMap = new HashMap<String, Registry<ConnectionSocketFactory>>();

    /**
     * 构造器
     */
    private HttpClientUtil() {
    }

    /**
     * 获取默认私钥实例，用于http请求、https单向认证请求、或默认证书的https双向认证请求
     * 默认私钥未配置时，返回实例仅适用于http请求、https单向认证请求
     *
     * @return
     */
    public static HttpClientUtil getInstance() {
        String keyStore = System.getProperty("ssl.keyStore");
        String storePass = System.getProperty("ssl.storePass");
        String storeType = System.getProperty("ssl.storeType");
        return getInstance(keyStore, storePass, storeType);
    }

    /**
     * 获取指定私钥实例，用于指定证书的https双向认证请求
     *
     * @param keyStore
     * @param keyPass
     * @param storeType JKS--默认,JCEKS, PKCS12 and PKCS11
     * @return
     */
    public static HttpClientUtil getInstance(String keyStore, String keyPass, String storeType) {
        HttpClientUtil instance = new HttpClientUtil();
        Registry<ConnectionSocketFactory> reg = instance.registry(keyStore, keyPass, storeType);
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(reg);
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(20);
        new IdleConnectionMonitorThread(connManager).start();

        // RequestConfig.Builder builder = RequestConfig.custom();
        // builder.setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT);
        // RequestConfig requestConfig = builder.build();
        // httpBuilder.setDefaultRequestConfig(requestConfig);

        // util.cookieStore = new BasicCookieStore();
        // httpBuilder.setDefaultCookieStore(util.cookieStore);
        instance.connManager = connManager;
        return instance;
    }

    /**
     * 指定编码字符集
     *
     * @param charset
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
        this.content_type = this.content_type.withCharset(charset);
    }

    public void setContent_type(ContentType content_type) {
        this.content_type = content_type;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 静态同步管理，避免重复加载证书
     *
     * @param keyStore
     * @param keyPass
     * @param storeType
     * @return
     */
    private Registry<ConnectionSocketFactory> registry(String keyStore, String keyPass, String storeType) {
        Registry<ConnectionSocketFactory> registry = null;
        synchronized (registryMap) {
            if (registryMap.containsKey(keyStore)) {
                registry = registryMap.get(keyStore);
            } else {
                RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
                ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
                registryBuilder.register("http", plainSF);
                // 指定信任密钥存储对象和连接套接字工厂
                try {
                    SSLContextBuilder sslBuilder = SSLContexts.custom().useTLS();
                    this.trustAllCertificate(sslBuilder);
                    if (!StringUtils.isEmpty(keyStore) && !StringUtils.isEmpty(keyPass)) {
                        loadClientCertificate(sslBuilder, keyStore, keyPass, storeType);
                    }
                    SSLContext sslContext = sslBuilder.build();
                    LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2", "SSLv2Hello", "SSLv3"}, null,
                            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    registryBuilder.register("https", sslSF);
                } catch (Exception e) {
                    logger.error("信任服务器证书发生错误", e);
                }
                registry = registryBuilder.build();
                registryMap.put(keyStore, registry);
            }
            registryMap.notify();
        }

        return registry;
    }

    /**
     * 加载客户端证书
     *
     * @param sslBuilder
     * @param keyStore
     * @param keyPass
     * @param storeType
     */
    private void loadClientCertificate(SSLContextBuilder sslBuilder, String keyStore, String keyPass, String storeType) {
        storeType = StringUtils.defaultIfBlank(storeType, KeyStore.getDefaultType());
        FileInputStream instream = null;
        try {
            instream = new FileInputStream(new File(keyStore));
            KeyStore keystore = KeyStore.getInstance(storeType);
            keystore.load(instream, keyPass.toCharArray());
            sslBuilder.loadKeyMaterial(keystore, keyPass.toCharArray()).build();
        } catch (Exception e) {
            logger.error("加载客户端证书发生错误", e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                logger.error("加载客户端证书发生错误", e);
            }
        }
    }

    /**
     * 信任所有服务器证书
     *
     * @param sslBuilder
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     */
    private void trustAllCertificate(SSLContextBuilder sslBuilder) throws KeyStoreException, NoSuchAlgorithmException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        sslBuilder.loadTrustMaterial(trustStore, new TrustStrategy() {

            // 信任所有
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        });
    }

    /**
     * 判断是否为Multipart请求
     *
     * @param params
     * @return
     */
    private boolean isMultipart(Map<String, Object> params) {
        if (params == null || params.size() < 0) {
            return false;
        }
        if (StringUtils.contains(this.content_type.toString(), "multipart/form-data")) {
            return true;
        }
        for (Entry<String, Object> entry : params.entrySet()) {
            Object pvalue = entry.getValue();
            if (pvalue == null) {
                continue;
            } else if (pvalue instanceof File || pvalue instanceof InputStreamBody || pvalue instanceof ByteArrayBody) {
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
                builder.addTextBody(pname, String.valueOf(pvalue), content_type);
                continue;
            }

            if (pvalue instanceof File) {
                builder.addBinaryBody(pname, (File) pvalue);
                continue;
            }

            if (pvalue instanceof MultipartFile) {
                MultipartFile multipartFile = (MultipartFile) pvalue;
                try {
                    builder.addPart(pname, new InputStreamBody(multipartFile.getInputStream(), content_type, multipartFile.getOriginalFilename()));
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
                    builder.addTextBody(pname, String.valueOf(obj), content_type);
                }
                continue;
            }

            if (pvalue.getClass().isArray()) {
                Object[] array = (Object[]) pvalue;
                for (int i = 0; i < array.length; i++) {
                    builder.addTextBody(pname, String.valueOf(array[i]), content_type);
                }
                continue;
            }

            // 兼容处理，long、int等基本类型
            builder.addTextBody(pname, String.valueOf(pvalue), content_type);
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
    public byte[] httpPost(String url, String xml, List<Header> headers) throws ClientProtocolException, IOException {
        HttpPost httppost = new HttpPost(url);
        if (StringUtils.contains(this.content_type.toString(), "x-www-form-urlencoded")) {
            this.setContent_type(ContentType.APPLICATION_XML.withCharset(charset));
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
    public byte[] httpPost(String url, String xml, ContentType content_type, List<Header> headers) throws ClientProtocolException, IOException {
        HttpPost httppost = new HttpPost(url);
        this.setContent_type(content_type.withCharset(charset));
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
        builder.setContentType(this.content_type);
        if (obj instanceof File) {
            builder.setFile((File) obj);
        } else if (obj instanceof InputStream) {
            builder.setStream((InputStream) obj);
        } else if (obj instanceof byte[]) {
            builder.setBinary((byte[]) obj);
        } else if (obj instanceof Map) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
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
        this.setContent_type(ContentType.APPLICATION_OCTET_STREAM.withCharset(charset));
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
    public byte[] httpGet(String url, List<Header> headers) throws ClientProtocolException, IOException {
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
    public byte[] httpPost(String url, List<Header> headers) throws ClientProtocolException, IOException {
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
            headers = new ArrayList<Header>();
        }

        HttpClientBuilder httpBuilder = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy());
        httpBuilder.setConnectionManager(connManager);
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            if (getProxyHost() != null) {
                httpclient = httpBuilder.setProxy(getProxyHost()).build();
            } else {
                httpclient = httpBuilder.build();
            }
            request.getParams().setParameter("Connection", "close");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();// 设置请求和传输超时时间
            request.setConfig(requestConfig);
            response = httpclient.execute(request);
            for (Header header : response.getAllHeaders()) {
                if ("Set-Cookie".equalsIgnoreCase(header.getName())) {
                    // 回写返回的cookie
                    headers.add(header);
                }
            }
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
                HttpEntity entity = response.getEntity();
                byte[] data = EntityUtils.toByteArray(entity);
                return data;
            } else {
                HttpEntity entity = response.getEntity();
                byte[] data = null;
                try {
                    data = EntityUtils.toByteArray(entity);
                } catch (Exception ignore) {
                    //
                }
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
            // 使用连接池，不能关闭httpclient
            // if (httpclient != null) {
            // try {
            // httpclient.close();
            // } catch (Exception e) {
            // logger.warn("关闭httpclient发生错误", e);
            // }
            // }
        }
    }

    /**
     * @param autoClose
     * @deprecated 设置连接自动关闭，默认为true；
     * 若设置为false，则连续调用可保持会话，使用后显示调用close方法；
     * 保持会话时传入header不会生效
     */
    public void setAutomaticClose(boolean autoClose) {
    }

    public HttpHost getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(HttpHost proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyHost(String hostname, int port) {
        this.proxyHost = new HttpHost(hostname, port);
    }

    /**
     * 垃圾回收时关闭
     */
    @Override
    public void finalize() {
        connManager.close();
    }
}
