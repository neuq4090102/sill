package com.elv.web.model;

import java.io.Serializable;
import java.util.Map;

import com.elv.core.constant.SortEnum;

/**
 * @author lxh
 * @since 2020-08-20
 */
public class SignParam implements Serializable {

    private static final long serialVersionUID = -7264085379566773183L;

    private Map<String, String> paramMap; // 参数
    private SortEnum sortEnum = SortEnum.ASC; // 排序
    private String algorithm; // 摘要算法
    private String secretName; // 密钥参数
    // @NotBlank
    private String secretKey; // 密钥

    public static SignParam of() {
        return new SignParam();
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public SortEnum getSortEnum() {
        return sortEnum;
    }

    public void setSortEnum(SortEnum sortEnum) {
        this.sortEnum = sortEnum;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public SignParam paramMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
        return this;
    }

    public SignParam sortEnum(SortEnum sortEnum) {
        this.sortEnum = sortEnum;
        return this;
    }

    public SignParam algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public SignParam secretName(String secretName) {
        this.secretName = secretName;
        return this;
    }

    public SignParam secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

}
