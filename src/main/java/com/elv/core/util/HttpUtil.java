package com.elv.core.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elv.core.model.util.HttpParam;

/**
 * @author lxh
 * @since 2020-06-16
 */
public final class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public static void main(String[] args) {

        // String uri = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15101568056";
        // String uri = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15232321504";
        // String uri = "https://www.baifubao.com/callback?cmd=1059&callback=phone&phone=15232321504";
        String uri = "localhost";

        int x = 2 ^ 2;
        System.out.println(x);
        // System.out.println(get(uri));

        StringBuilder sb = new StringBuilder(null);
        System.out.println(sb.toString());
    }

    public static String get(String uri) {
        return get(uri, UTF8);
    }

    public static String get(String uri, Charset charset) {
        HttpParam httpParam = new HttpParam();
        httpParam.setUri(uri);
        httpParam.setCharset(charset);
        return get(httpParam);
    }

    public static String get(HttpParam httpParam) {

        initParam(httpParam);

        HttpGet httpGet = new HttpGet(getURI(httpParam));

        CloseableHttpClient httpClient = getClient();
        CloseableHttpResponse httpResponse = null;
        try {
            httpGet.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, Optional.ofNullable(httpParam.getCharset()).orElse(UTF8));
        } catch (IOException e) {
            throw new RuntimeException("HttpUtil failed to get.", e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    logger.error("HttpUtil failed to close httpResponse.", e);
                }
            }

            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("HttpUtil failed to close httpClient.", e);
                }
            }
        }
    }

    public static String post(HttpParam httpParam) {

        initParam(httpParam);

        // 设置请求头
        // List<Header> headers = headerList;
        // if (headers == null) {
        //     headers = new ArrayList<Header>(HttpUtils.HEADER_LIST);
        // }

        HttpPost httpPost = new HttpPost(httpParam.getUri());
        CloseableHttpClient httpClient = getClient();
        CloseableHttpResponse httpResponse = null;
        try {
            // Header[] headers = new Header[] { new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8"),
            //         new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"),
            //         new BasicHeader("Content-Type", "application/x-www-form-urlencoded"), new BasicHeader("User-Agent",
            //         "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36") };
            // httpPost.setHeaders(headers);
            httpPost.setEntity(new UrlEncodedFormEntity(httpParam.getPairs()));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, Optional.ofNullable(httpParam.getCharset()).orElse(UTF8));
        } catch (IOException e) {
            throw new RuntimeException("HttpUtil failed to get.", e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    logger.error("HttpUtil failed to close httpResponse.", e);
                }
            }

            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("HttpUtil failed to close httpClient.", e);
                }
            }
        }
    }

    private static CloseableHttpClient getClient() {
        // https://stackoverflow.com/questions/7459279/httpclient-warning-cookie-rejected-illegal-domain-attribute
        return HttpClientBuilder.create().disableCookieManagement()
                .build(); // resolve warning:Cookie rejected: Illegal domain attribute
        // return HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
        // return HttpClients.createDefault(); // TODO:
    }

    private static void initParam(HttpParam httpParam) {
        List<NameValuePair> pairs = new ArrayList<>();
        if (httpParam.getParamMap() != null) {
            httpParam.getParamMap().forEach((key, value) -> {
                pairs.add(new BasicNameValuePair(key, value));
            });
        }

        if (httpParam.getPairs() == null) {
            httpParam.setPairs(pairs);
        } else {
            httpParam.getPairs().addAll(pairs);
        }
    }

    private static String getURI(HttpParam httpParam) {
        if (httpParam.getUri() == null) {
            return "";
        }

        String uri = httpParam.getUri();
        if (httpParam.getPairs().size() > 0) {
            StringBuilder sb = new StringBuilder(uri.split("\\?")[0]);
            for (int i = 0; i < httpParam.getPairs().size(); i++) {
                NameValuePair pair = httpParam.getPairs().get(i);
                sb.append(i == 0 ? "?" : "&").append(pair.getName()).append("=").append(pair.getValue());
            }
            uri = sb.toString();
        }

        return uri;
    }

}
