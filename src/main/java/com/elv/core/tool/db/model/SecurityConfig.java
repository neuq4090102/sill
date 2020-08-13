package com.elv.core.tool.db.model;

/**
 * 密钥配置
 *
 * @author lxh
 * @since 2020-08-13
 */
public class SecurityConfig {

    private long id;
    private String name; // 名称
    private String alias; // 别名
    private String secretKey; // 密钥
    private String iv; // 初始化向量
    private String algorithm; // 密钥算法

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
