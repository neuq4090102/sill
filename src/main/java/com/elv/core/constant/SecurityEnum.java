package com.elv.core.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author lxh
 * @since 2020-08-07
 */
public class SecurityEnum {

    public static final Charset UTF8;

    static {
        UTF8 = StandardCharsets.UTF_8;
    }

    private SecurityEnum() {
    }

    /**
     * 算法
     */
    public enum Algorithm {
        MD5("MD5", AbsAlgo.MD, ""), //
        SHA("SHA-1", AbsAlgo.MD, ""), //
        SHA256("SHA-256", AbsAlgo.MD, ""), //
        SHA512("SHA-512", AbsAlgo.MD, ""), //

        HmacMD5("HmacMD5", AbsAlgo.MAC, ""), //
        HmacSHA1("HmacSHA1", AbsAlgo.MAC, ""), //
        HmacSHA256("HmacSHA256", AbsAlgo.MAC, ""), //
        HmacSHA512("HmacSHA512", AbsAlgo.MAC, ""), //

        DES("DES", AbsAlgo.SE, "DES/CBC/PKCS5Padding"),  //
        DESede("DESede", AbsAlgo.SE, "DESede/CBC/PKCS5Padding"),  //
        AES("AES", AbsAlgo.SE, "AES/CBC/PKCS5Padding"), //

        RSA("RSA", AbsAlgo.AE, ""), //
        DSA("DSA", AbsAlgo.AE, ""), // 仅用于签名算法中的密钥算法

        MD5withRSA("MD5withRSA", AbsAlgo.DS, ""), //
        SHA1withRSA("SHA1withRSA", AbsAlgo.DS, ""), //
        SHA256withRSA("SHA256withRSA", AbsAlgo.DS, ""), // SHA512/256withRSA
        SHA512withRSA("SHA512withRSA", AbsAlgo.DS, ""), //
        SHA1withDSA("SHA1withDSA", AbsAlgo.DS, ""), //
        SHA256withDSA("SHA256withDSA", AbsAlgo.DS, ""), //
        SHA512withDSA("SHA512withDSA", AbsAlgo.DS, ""), //
        ;
        private final String val;
        private final AbsAlgo algo;
        private final String cipher; // 加密/解密算法：算法/模式/填充方式

        Algorithm(String val, AbsAlgo algo, String cipher) {
            this.val = val;
            this.algo = algo;
            this.cipher = cipher;
        }

        public String getVal() {
            return val;
        }

        public String getCipher() {
            return cipher;
        }

        public AbsAlgo getAlgo() {
            return algo;
        }

        public boolean belong(AbsAlgo algo) {
            return this.getAlgo() == algo;
        }
    }

    public enum AbsAlgo {

        /**
         * 摘要算法
         * <p>
         * Java8支持的算法有：[MD2, MD5, SHA-1, SHA-224, SHA-256, SHA-384, SHA-512]
         */
        MD,

        /**
         * 消息认证码
         * <p>
         * Java8支持的算法
         *
         * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyGenerator">密钥算法</a>
         */
        MAC,

        /**
         * 对称加密
         */
        SE,

        /**
         * 非对称加密
         */
        AE,

        /**
         * 签名算法
         * <p>
         * Java8支持的算法
         *
         * @see <a href=https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Signature>签名算法</a>
         */
        DS,

    }
}
