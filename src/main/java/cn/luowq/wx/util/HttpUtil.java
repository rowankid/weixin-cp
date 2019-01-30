package cn.luowq.wx.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: rowan
 * @Date: 2019/1/24 17:20
 * @Description:
 */
public class HttpUtil {

    private static final String CHARSET = "UTF-8";

    public static String getResponseBody(String url, Map<String, String> params, Map headers) throws Exception {
        SSLContext sslContext = createIgnoreVerifySSL();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslContext))
            .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connectionManager);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(120000)
            .setSocketTimeout(60000)
            .setConnectionRequestTimeout(60000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
            .setRedirectStrategy(new DefaultRedirectStrategy())
            .setDefaultRequestConfig(requestConfig)
            .setConnectionManager(connectionManager)
            .setDefaultCookieStore(new BasicCookieStore()).build();
        CloseableHttpResponse response = null;
        if (params == null) {
            HttpGet httpGet = new HttpGet(url);
            if (Objects.nonNull(headers)) {
                for (Object key : headers.keySet()) {
                    httpGet.setHeader(key.toString(), headers.get(key).toString());
                }
            }
            response = httpClient.execute(httpGet);
        } else {
//            Map<String, String> map = HttpClientUtil.initParamMap(params);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if (Objects.nonNull(headers)) {
                for (Object key : headers.keySet()) {
                    httpPost.setHeader(key.toString(), headers.get(key).toString());
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, CHARSET));
            HttpClientContext context = HttpClientContext.create();
            response = httpClient.execute(httpPost, context);
        }

        if (response.getStatusLine().getStatusCode() != 200) {
            StatusLine statusLine = response.getStatusLine();
            return "{'errorCode'：'" + statusLine.getStatusCode() + "','errorMsg':'" + statusLine.getReasonPhrase() + "'}";
        }
        HttpEntity entity = response.getEntity();
        String body = "";
        try {
            if (entity != null) {
                body = EntityUtils.toString(entity, CHARSET);
            }
            EntityUtils.consume(entity);
            response.close();
        } finally {
            response.close();
        }
        return body;
    }

    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
}
