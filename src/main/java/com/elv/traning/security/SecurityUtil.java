package com.elv.traning.security;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import com.elv.core.constant.SecurityEnum;
import com.elv.core.constant.SecurityEnum.Algorithm;

/**
 * @author lxh
 * @since 2020-06-16
 */
public class SecurityUtil {

    private static final SecureRandom RANDOM;

    static {
        RANDOM = new SecureRandom();
    }

    private SecurityUtil() {
    }

    /**
     * base64编码
     *
     * @param bytes 数据字节码
     * @return java.lang.String
     */
    public static String base64Encode(byte[] bytes) {
        return Base64Util.encode(bytes);
    }

    /**
     * base64编码
     *
     * @param str 待编码数据
     * @return java.lang.String
     */
    public static String base64Encode(String str) {
        return Base64Util.encode(str, SecurityEnum.UTF8);
    }

    /**
     * base64编码
     *
     * @param str 待编码数据
     * @param cs  字符集
     * @return java.lang.String
     */
    public static String base64Encode(String str, Charset cs) {
        return Base64Util.encode(str, cs);
    }

    /**
     * base64解码
     *
     * @param base64Str 待解码数据
     * @return java.lang.String
     */
    public static String base64Decode(String base64Str) {
        return Base64Util.decode(base64Str, SecurityEnum.UTF8);
    }

    /**
     * base64解码
     *
     * @param base64Str 待解码数据
     * @param cs        字符集
     * @return java.lang.String
     */
    public static String base64Decode(String base64Str, Charset cs) {
        return Base64Util.decode(base64Str, cs);
    }

    /**
     * base64解码
     *
     * @param base64Str 待解码数据
     * @return byte[]
     */
    public static byte[] base64DecodeToBytes(String base64Str) {
        return Base64Util.decodeToBytes(base64Str);
    }

    /**
     * MD5消息摘要（不推荐使用）
     *
     * @param msg 消息
     * @return java.lang.String，16进制
     */
    public static String md5(String msg) {
        return MDUtil.mdBy(msg, Algorithm.MD5.getVal());
    }

    /**
     * MD5消息摘要（不推荐使用）
     *
     * @param msg 消息
     * @param cs  字符集
     * @return java.lang.String，16进制
     */
    public static String md5(String msg, Charset cs) {
        return MDUtil.mdBy(msg, Algorithm.MD5.getVal(), cs);
    }

    /**
     * SHA消息摘要（不推荐使用）
     *
     * @param msg 消息
     * @return java.lang.String，16进制
     */
    public static String sha(String msg) {
        return MDUtil.mdBy(msg, Algorithm.SHA.getVal());
    }

    /**
     * SHA消息摘要（不推荐使用）
     *
     * @param msg 消息
     * @param cs  字符集
     * @return java.lang.String，16进制
     */
    public static String sha(String msg, Charset cs) {
        return MDUtil.mdBy(msg, Algorithm.SHA.getVal(), cs);
    }

    /**
     * SHA-256消息摘要（推荐使用）
     *
     * @param msg 消息
     * @return java.lang.String，16进制
     */
    public static String sha256(String msg) {
        return MDUtil.mdBy(msg, Algorithm.SHA256.getVal());
    }

    /**
     * SHA-512消息摘要（推荐使用）
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
        return MDUtil.mdBy(msg, algorithm);
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
        return MDUtil.mdBy(msg, algorithm, cs);
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
        return MACUtil.mac(msg, algorithm, secretKey);
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
        return MACUtil.mac(msg, algorithm, secretKey, cs);
    }

    /**
     * 验证消息认证码
     *
     * @param msg         消息
     * @param algorithm   MAC算法
     * @param secretKey   密钥
     * @param receivedMac 消息认证码(16进制)
     * @param cs          字符集
     * @return boolean
     */
    public static boolean macVerify(String msg, String algorithm, String secretKey, String receivedMac, Charset cs) {
        return MACUtil.mac(msg, algorithm, secretKey, cs).equals(receivedMac);
    }

    /**
     * AES加密
     *
     * @param plaintext 明文
     * @param secretKey 密钥
     * @param iv        初始化向量
     * @return java.lang.String
     */
    public static String encryptByAES(String plaintext, String secretKey, String iv) {
        return SEUtil.encrypt(plaintext, Algorithm.AES, secretKey, iv, SecurityEnum.UTF8);
    }

    /**
     * AES解密
     *
     * @param hexCiphertext 密文(16进制)
     * @param secretKey     密钥
     * @param iv            初始化向量
     * @return java.lang.String
     */
    public static String decryptByAES(String hexCiphertext, String secretKey, String iv) {
        return SEUtil.decrypt(hexCiphertext, Algorithm.AES, secretKey, iv, SecurityEnum.UTF8);
    }

    /**
     * RSA签名
     *
     * @param msg           消息
     * @param signAlgorithm 签名算法
     * @param privateKey    私钥(base64)
     * @param cs            字符集
     * @return java.lang.String
     */
    public static String signByRSA(String msg, Algorithm signAlgorithm, String privateKey, Charset cs) {
        return DSUtil.signByRSA(msg, signAlgorithm, privateKey, cs);
    }

    /**
     * DSA签名
     *
     * @param msg           消息
     * @param signAlgorithm 签名算法
     * @param privateKey    私钥(base64)
     * @param cs            字符集
     * @return java.lang.String
     */
    public static String signByDSA(String msg, Algorithm signAlgorithm, String privateKey, Charset cs) {
        return DSUtil.signByDSA(msg, signAlgorithm, privateKey, cs);
    }

    /**
     * 自定义数字签名
     *
     * @param msg           消息
     * @param keyAlgorithm  密钥算法
     * @param signAlgorithm 签名算法
     * @param privateKey    私钥(base64)
     * @return java.lang.String
     */
    public static String signBy(String msg, Algorithm keyAlgorithm, Algorithm signAlgorithm, String privateKey) {
        return DSUtil.signBy(msg, keyAlgorithm, signAlgorithm, privateKey);
    }

    /**
     * RSA签名验证
     *
     * @param msg              消息
     * @param signAlgorithm    签名算法
     * @param publicKey        公钥(base64)
     * @param digitalSignature 数字签名内容(base64)
     * @param cs               字符集
     * @return java.lang.String
     */
    public static boolean verifyByRSA(String msg, Algorithm signAlgorithm, String publicKey, String digitalSignature,
            Charset cs) {
        return DSUtil.verifyByRSA(msg, signAlgorithm, publicKey, digitalSignature, cs);
    }

    /**
     * DSA签名验证
     *
     * @param msg              消息
     * @param signAlgorithm    签名算法
     * @param publicKey        公钥(base64)
     * @param digitalSignature 数字签名内容(base64)
     * @param cs               字符集
     * @return java.lang.String
     */
    public static boolean verifyByDSA(String msg, Algorithm signAlgorithm, String publicKey, String digitalSignature,
            Charset cs) {
        return DSUtil.verifyByDSA(msg, signAlgorithm, publicKey, digitalSignature, cs);
    }

    /**
     * 自定义签名验证
     *
     * @param msg              消息
     * @param keyAlgorithm     密钥算法
     * @param signAlgorithm    签名算法
     * @param publicKey        公钥(base64)
     * @param digitalSignature 数字签名内容(base64)
     * @return java.lang.String
     */
    public static boolean verifyBy(String msg, Algorithm keyAlgorithm, Algorithm signAlgorithm, String publicKey,
            String digitalSignature) {
        return DSUtil.verifyBy(msg, keyAlgorithm, signAlgorithm, publicKey, digitalSignature);
    }

    /**
     * 生成16进制字符串
     *
     * @param bytes 字节数组
     * @return java.lang.String
     * @since 2020-07-30
     */
    public static String toHex(byte[] bytes) {
        return NumerationUtil.toHex(bytes);
    }

    /**
     * 解析16进制内容
     *
     * @param hexStr 16进制字符串
     * @param cs     字符集
     * @return java.lang.String
     * @see #toHex
     */
    public static String hexToStr(String hexStr, Charset cs) {
        return new String(NumerationUtil.toBytes(hexStr), cs);
    }

    /**
     * 盐值
     *
     * @return java.lang.String
     */
    public static String salt() {
        return NumerationUtil.toHex(RANDOM.generateSeed(8));
    }

    /**
     * 盐值
     *
     * @param numBytes 指定盐值长度
     * @return java.lang.String
     */
    public static String salt(int numBytes) {
        return NumerationUtil.toHex(RANDOM.generateSeed(numBytes));
    }

    public static void main(String[] args) {
        MDUtil.sha256("333");
        System.out.println(salt(16));
    }

}
