package com.elv.core.tool.db.model;

/**
 * 授权信息
 *
 * @author lxh
 * @since 2020-08-13
 */
public class Authorization {

    private String source; // 来源
    private String tool; // 对接工具
    private String appId; // 唯一识别码
    private String appSecret; // 密钥
    private String apiKey; // api key

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
