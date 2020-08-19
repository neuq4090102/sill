package com.elv.core.tool.security.impl;

import java.nio.charset.Charset;

import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;
import com.elv.core.tool.security.AbstractMACUtil;
import com.elv.core.tool.security.NumerationUtil;

/**
 * 消息认证码
 *
 * @author lxh
 * @since 2020-08-18
 */
public class MACUtil extends AbstractMACUtil {

    private String algorithm; // 密钥算法
    private String secretKey; // 密钥

    private MACUtil() {
    }

    public static MACUtil of() {
        return new MACUtil();
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public byte[] getSecretKeys() {
        return secretKey.getBytes(cs);
    }

    public MACUtil algorithm(Algorithm algorithm) {
        Assert.isTrue(!algorithm.belong(AbsAlgo.MAC), "MACUtil#algorithm invalid algorithm:" + algorithm.getVal());
        this.algorithm = algorithm.getVal();
        return this;
    }

    public MACUtil algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public MACUtil secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public MACUtil cs(Charset cs) {
        this.cs = cs;
        return this;
    }

    /**
     * 消息认证码
     *
     * @param msg 消息
     * @return java.lang.String
     */
    public String mac(String msg) {
        Assert.notBlank("MACUtil#sign param not blank.", msg, algorithm, secretKey);
        init();
        return NumerationUtil.toHex(mac(msg.getBytes(cs)));
    }

    /**
     * 验证消息认证码
     *
     * @param msg      消息
     * @param macValue 消息认证码(16进制)
     * @return boolean
     */
    public boolean verify(String msg, String macValue) {
        Assert.notBlank("MACUtil#verify param not blank.", msg, macValue, algorithm, secretKey);
        init();
        return NumerationUtil.toHex(mac(msg.getBytes(cs))).equals(macValue);
    }

    public static void main(String[] args) {
        testActualMAC();
    }

    private static void testActualMAC() {
        Algorithm algorithm = Algorithm.HmacSHA512; // 算法（发送方和接收方约定）
        String secretKey = "66666";  // 密钥（发送方和接收方共享）
        String msg = "这是一条会话消息"; // 消息
        String macValue = MACUtil.of().algorithm(algorithm).secretKey(secretKey).mac(msg); // 发送方生成

        System.out.println(secretKey);
        System.out.println(macValue);

        // 发送方把消息和MAC值都发给接收方
        msg = msg + ",窃取并篡改。";
        boolean success = MACUtil.of().algorithm(algorithm).secretKey(secretKey).verify(msg, macValue);// 接收方验证
        if (success) {
            System.out.println("验证成功，消息未被更改");
        } else {
            System.out.println("验证失败，消息已被更改");
        }
    }
}

