package com.elv.traning.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Optional;

import com.elv.core.constant.SecurityEnum;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;

/**
 * 消息摘要（Message Digest）
 *
 * @author lxh
 * @since 2020-08-07
 */
public class MDUtil {

    private MDUtil() {
    }

    /**
     * MD5消息摘要
     *
     * @param msg 消息
     * @return java.lang.String，16进制
     */
    public static String md5(String msg) {
        return MDUtil.mdBy(msg, Algorithm.MD5.getVal());
    }

    /**
     * MD5消息摘要
     *
     * @param msg 消息
     * @param cs  字符集
     * @return java.lang.String，16进制
     */
    public static String md5(String msg, Charset cs) {
        return MDUtil.mdBy(msg, Algorithm.MD5.getVal(), cs);
    }

    /**
     * SHA消息摘要
     *
     * @param msg 消息
     * @return java.lang.String，16进制
     */
    public static String sha(String msg) {
        return MDUtil.mdBy(msg, Algorithm.SHA.getVal());
    }

    /**
     * SHA消息摘要
     *
     * @param msg 消息
     * @param cs  字符集
     * @return java.lang.String，16进制
     */
    public static String sha(String msg, Charset cs) {
        return MDUtil.mdBy(msg, Algorithm.SHA.getVal(), cs);
    }

    /**
     * SHA-256消息摘要
     *
     * @param msg 消息
     * @return java.lang.String，16进制
     */
    public static String sha256(String msg) {
        return MDUtil.mdBy(msg, Algorithm.SHA256.getVal());
    }

    /**
     * SHA-512消息摘要
     *
     * @param msg 消息
     * @return java.lang.String，16进制
     */
    public static String sha512(String msg) {
        return MDUtil.mdBy(msg, Algorithm.SHA512.getVal());
    }

    /**
     * 自定义消息摘要
     *
     * @param msg       消息
     * @param algorithm 消息摘要算法
     * @return java.lang.String
     */
    public static String mdBy(String msg, String algorithm) {
        Assert.notBlank(msg, "MDUtil#mdBy msg not blank.");
        Assert.notBlank(algorithm, "MDUtil#mdBy algorithm not blank.");

        return NumerationUtil.toHex(md(msg.getBytes(SecurityEnum.UTF8), algorithm));

    }

    /**
     * 自定义消息摘要
     *
     * @param msg       消息
     * @param algorithm 消息摘要算法
     * @param cs        字符集
     * @return java.lang.String，16进制
     */
    public static String mdBy(String msg, String algorithm, Charset cs) {
        Assert.notBlank(msg, "MDUtil#mdBy msg not blank.");
        Assert.notBlank(algorithm, "MDUtil#mdBy algorithm not blank.");

        cs = Optional.ofNullable(cs).orElse(SecurityEnum.UTF8);

        return NumerationUtil.toHex(md(msg.getBytes(cs), algorithm));
    }

    /**
     * 消息摘要
     *
     * @param msg       消息
     * @param algorithm 摘要算法
     * @return byte[]
     */
    public static byte[] md(byte[] msg, String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm).digest(msg);
        } catch (Exception e) {
            throw new RuntimeException("MDUtil#md MD algorithm [" + algorithm + "] not found.");
        }
    }

    public static void main(String[] args) {
        testMd();
    }

    private static void testMd() {
        String msg = "java";

        System.out.println(md5(msg));
        System.out.println(md5(msg, null));

        System.out.println(sha(msg));
        System.out.println(sha(msg, SecurityEnum.UTF8));

        System.out.println(sha256(msg));
        System.out.println("sha-256的长度：" + sha256(msg).length());

        System.out.println(sha512(msg));
        System.out.println("sh512的长度：" + sha512(msg).length());

        // System.out.println(mdBy(msg, "sha2", SecurityEnum.UTF8));
    }
}
