package com.elv.core.tool.security;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

import com.elv.core.constant.SecurityEnum;

/**
 * Base64
 *
 * @author lxh
 * @since 2020-08-07
 */
public class Base64Util {

    private Base64Util() {
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

    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(SecurityEnum.UTF8));
    }

    public static String encode(String str, Charset cs) {
        return Base64.getEncoder().encodeToString(str.getBytes(Optional.ofNullable(cs).orElse(SecurityEnum.UTF8)));
    }

    public static byte[] decodeToBytes(String base64Str) {
        return Base64.getDecoder().decode(base64Str);
    }

    public static String decode(String str) {
        return new String(Base64.getDecoder().decode(str), SecurityEnum.UTF8);
    }

    public static String decode(String str, Charset cs) {
        return new String(Base64.getDecoder().decode(str), Optional.ofNullable(cs).orElse(SecurityEnum.UTF8));
    }

    public static void main(String[] args) {
        testBase64();
    }

    private static void testBase64() {
        String str = "java base64";
        System.out.println(base64Encode(str));
        System.out.println(base64Decode("amF2YQ=="));

        for (byte item : str.getBytes()) {
            System.out.println(item);
        }
    }

}
