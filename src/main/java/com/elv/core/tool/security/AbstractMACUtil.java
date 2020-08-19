package com.elv.core.tool.security;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 消息认证码（Message Authentication Code）
 *
 * @author lxh
 * @since 2020-08-18
 */
public abstract class AbstractMACUtil extends BasicSecurity {

    /**
     * MAC算法
     *
     * @return java.lang.String
     */
    public abstract String getAlgorithm();

    /**
     * 密钥
     *
     * @return byte[]
     */
    public abstract byte[] getSecretKeys();

    /**
     * 消息认证码
     *
     * @param msg 消息
     * @return byte[]
     */
    public byte[] mac(byte[] msg) {
        try {
            SecretKey secretKey = new SecretKeySpec(getSecretKeys(), getAlgorithm());
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return mac.doFinal(msg);
        } catch (Exception e) {
            throw new RuntimeException("AbstractMACUtil#mac error.", e);
        }
    }

}
