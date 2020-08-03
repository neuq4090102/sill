package com.elv.traning.sensitiveWord.baidu;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.elv.core.model.util.HttpParam;
import com.elv.core.util.Dater;
import com.elv.core.util.HttpUtil;

/**
 * @author lxh
 * @since 2020-07-29
 */
public class BaiduToolDrill {

    //设置APPID/AK/SK
    public static final String APP_ID = "21672001";
    public static final String API_KEY = "U8W4Uqb9xME5PgCSXB6Ywxtu";
    public static final String SECRET_KEY = "xKk6wBpmi8GLpgOI2PjE1X2NE95EhW2mf";

    public static void main(String[] args) {

        String s = HttpUtil
                .get("https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?text=%E7%99%BE%E5%BA%A6%E6%98%AF%E5%82%BB%E9%80%BC&access_token=24.840a425cb9ea294e38febc0bc646c5e5.2592000.1598605225.282335-21672001");
        System.out.println(s);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("text", "百度是傻逼");
        paramMap.put("content", "百度是傻逼");
        paramMap.put("type", "textcensor");
        paramMap.put("apiType", "censor");
        paramMap.put("requestTime", Dater.now().getInstant().toEpochMilli() + "");
        paramMap.put("access_token", "24.840a425cb9ea294e38febc0bc646c5e5.2592000.1598605225.282335-21672001");

        HttpParam httpParam = new HttpParam();
        // httpParam.setUri("https://ai.baidu.com/aidemo");
        httpParam.setUri("https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined");
        httpParam.setParamMap(paramMap);
        String post = HttpUtil.post(httpParam);

        System.out.println(post);

    }

    private static void fetchBaiduToken() {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("grant_type", "client_credentials");
        paramMap.put("client_id", API_KEY);
        paramMap.put("client_secret", SECRET_KEY);

        HttpParam httpParam = new HttpParam();
        httpParam.setUri("https://aip.baidubce.com/oauth/2.0/token");
        httpParam.setParamMap(paramMap);
        String post = HttpUtil.post(httpParam);

        Locale locale;
        System.out.println(post);
    }
}
