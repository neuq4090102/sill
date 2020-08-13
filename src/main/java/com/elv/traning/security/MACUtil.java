package com.elv.traning.security;

import java.nio.charset.Charset;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.elv.core.constant.SecurityEnum;
import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;

/**
 * 消息认证码（Message Authentication Code）
 *
 * @author lxh
 * @since 2020-08-07
 */
public class MACUtil {

    private MACUtil() {
    }

    /**
     * 消息认证码
     *
     * @param msg       消息
     * @param algorithm MAC算法
     * @param hexKeyStr 密钥(16进制)
     * @return java.lang.String
     */
    public static String fetchMAC(String msg, Algorithm algorithm, String hexKeyStr) {
        check(algorithm);
        byte[] macBytes = mac(msg.getBytes(SecurityEnum.UTF8), algorithm.getVal(), NumerationUtil.toBytes(hexKeyStr));
        return NumerationUtil.toHex(macBytes);
    }

    /**
     * 消息认证码
     *
     * @param msg       消息
     * @param algorithm MAC算法
     * @param secretKey 密钥
     * @return java.lang.String
     */
    public static String mac(String msg, Algorithm algorithm, String secretKey) {
        check(algorithm);
        return mac(msg, algorithm.getVal(), secretKey, null);
    }

    /**
     * 消息认证码
     *
     * @param msg       消息
     * @param algorithm MAC算法
     * @param secretKey 密钥
     * @param cs        字符集
     * @return java.lang.String
     */
    public static String mac(String msg, String algorithm, String secretKey, Charset cs) {
        Assert.notBlank(msg, "MACUtil#mac msg not blank.");
        Assert.notBlank(algorithm, "MACUtil#mac algorithm not blank.");
        Assert.notBlank(secretKey, "MACUtil#mac secretKey not blank.");

        cs = Optional.ofNullable(cs).orElse(SecurityEnum.UTF8);

        byte[] macBytes = mac(msg.getBytes(cs), algorithm, secretKey.getBytes(cs));

        return NumerationUtil.toHex(macBytes);
    }

    /**
     * 消息认证码
     *
     * @param msg        消息
     * @param algorithm  MAC算法
     * @param secretKeys 密钥
     * @return byte[]
     */
    public static byte[] mac(byte[] msg, String algorithm, byte[] secretKeys) {
        try {
            SecretKey secretKey = new SecretKeySpec(secretKeys, algorithm);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return mac.doFinal(msg);
        } catch (Exception e) {
            throw new RuntimeException("MACUtil#mac error.", e);
        }
    }

    private static void check(Algorithm algorithm) {
        Assert.notNull(algorithm, "MACUtil#check algorithm not null.");
        Assert.isTrue(!algorithm.belong(AbsAlgo.MAC), "MACUtil#check invalid algorithm:" + algorithm.getVal());
    }

    /**
     * 验证消息认证码
     *
     * @param msg         消息
     * @param algorithm   MAC算法
     * @param hexKeyStr   密钥(16进制)
     * @param receivedMac 消息认证码(base64)
     * @return boolean
     */
    private static boolean verifyMAC(String msg, Algorithm algorithm, String hexKeyStr, String receivedMac) {
        return fetchMAC(msg, algorithm, hexKeyStr).equals(receivedMac);
    }

    /**
     * 验证消息认证码
     *
     * @param msg         消息
     * @param algorithm   MAC算法
     * @param secretKey   密钥
     * @param receivedMac 消息认证码(base64)
     * @return boolean
     */
    public static boolean verify(String msg, String algorithm, String secretKey, String receivedMac) {
        return mac(msg, algorithm, secretKey, SecurityEnum.UTF8).equals(receivedMac);
    }

    public static void main(String[] args) {
        testMAC();
        testActualMAC();
    }

    private static void testMAC() {
        Algorithm algorithm = Algorithm.HmacMD5; // 算法（发送方和接收方约定）
        String secretKey = initKey(algorithm.getVal());  // 密钥（发送方和接收方共享）
        String msg = "这是一条会话消息"; // 消息
        String macValue = fetchMAC(msg, algorithm, secretKey); // 发送方生成

        System.out.println(secretKey);
        // ee370b8bc186c94e2db47fcb66ff7c961d1ca3dec25b6f6bfa90f6cde61d13a69c91ddfb0142778a20c58d7c91363e302b4b325f366f530ba1b24dbae7ea0856
        // 3b18ac9efa25b893a25becf71221c1987dc612defe59fffc33bff76329b4988f19127a674f38ca02f3942c7ea71831ad0918e43fc5e8a2bddf3168151b26360f
        System.out.println(macValue);

        // 发送方把消息和MAC值都发给接收方
        msg = msg + ",窃取并篡改。";
        boolean success = verifyMAC(msg, algorithm, secretKey, macValue); // 接收方验证
        if (success) {
            System.out.println("验证成功，消息未被更改");
        } else {
            System.out.println("验证失败，消息已被更改");
        }
    }

    private static void testActualMAC() {
        Algorithm algorithm = Algorithm.HmacSHA512; // 算法（发送方和接收方约定）
        String secretKey = "66666";  // 密钥（发送方和接收方共享）
        String msg = "这是一条会话消息"; // 消息
        String macValue = mac(msg, algorithm, secretKey); // 发送方生成

        System.out.println(secretKey);
        System.out.println(macValue);

        // 发送方把消息和MAC值都发给接收方
        // msg = msg + ",窃取并篡改。";
        boolean success = verify(msg, algorithm.getVal(), secretKey, macValue);// 接收方验证
        if (success) {
            System.out.println("验证成功，消息未被更改");
        } else {
            System.out.println("验证失败，消息已被更改");
        }
    }

    private static String initKey(String algorithm) {
        return NumerationUtil.toHex(KeyUtil.initKey(algorithm).getEncoded());
    }
}
