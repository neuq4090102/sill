package com.elv.core.tool.text.model;

/**
 * @author lxh
 * @since 2020-07-29
 */
public class BaiduTokenResult {

    // 失败返回结果
    private String error; //  错误码；
    private String error_description; //  错误描述信息

    // 成功返回结果
    private String refresh_token;
    private String expires_in; // Access Token的有效期(秒为单位，一般为1个月)
    private String session_key;
    private String access_token; // 有效期为30天
    private String scope;
    private String session_secret;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }
}
