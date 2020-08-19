package com.elv.core.tool.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * 进制工具
 *
 * @author lxh
 * @since 2020-08-07
 */
public class NumerationUtil {

    /**
     * 转化为16进制
     *
     * @param bytes 字节数组
     * @return java.lang.String
     */
    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(toHex(b));
        }

        return sb.toString();
    }

    /**
     * 转化为16进制
     *
     * @param b 字节
     * @return java.lang.String
     */
    public static String toHex(byte b) {
        String hex = Integer.toHexString(b & 0xff);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 转化为字节数组
     *
     * @param hexStr 16进制字符串
     * @return byte[]
     */
    public static byte[] toBytes(String hexStr) {
        try {
            return Hex.decodeHex(hexStr);
        } catch (DecoderException e) {
            throw new RuntimeException("NumerationUtil#toBytes error.", e);
        }
    }
}
