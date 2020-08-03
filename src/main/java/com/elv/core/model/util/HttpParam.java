package com.elv.core.model.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

/**
 * @author lxh
 * @since 2020-06-29
 */
public final class HttpParam {

    // TODO : public static class Builder {

    private Charset charset = StandardCharsets.UTF_8;
    private String uri; // 注意：是URI，而不是URL
    private Map<String, String> paramMap;
    private List<NameValuePair> pairs;
    private int timeout = 5000; // 单位：毫秒

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public List<NameValuePair> getPairs() {
        return pairs;
    }

    public void setPairs(List<NameValuePair> pairs) {
        this.pairs = pairs;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
