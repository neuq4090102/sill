package com.elv.core.tool.security;

import java.security.MessageDigest;

/**
 * 消息摘要（Message Digest）
 *
 * @author lxh
 * @since 2020-08-18
 */
public abstract class AbstractMDUtil extends BasicSecurity {

    /**
     * 消息摘要算法
     *
     * @return java.lang.String
     */
    public abstract String getAlgorithm();

    /**
     * 消息摘要
     *
     * @param msg 消息
     * @return byte[]
     */
    public byte[] md(byte[] msg) {
        try {
            return MessageDigest.getInstance(getAlgorithm()).digest(msg);
        } catch (Exception e) {
            throw new RuntimeException("AbstractMDUtil#md error.");
        }
    }
}
