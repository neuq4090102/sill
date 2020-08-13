package com.elv.traning.security;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.Optional;

import com.elv.core.constant.SecurityEnum;
import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;

/**
 * 数字签名（Digital Signature）
 * <p>
 * 可看作是公钥加密的逆过程
 *
 * @author lxh
 * @since 2020-08-07
 */
public class DSUtil {

    public static final int KEY_SIZE_RSA = 2048;

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
        check(Algorithm.RSA, signAlgorithm);
        return signBy(msg, Algorithm.RSA.getVal(), signAlgorithm.getVal(), privateKey, cs);
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
        check(Algorithm.DSA, signAlgorithm);
        return signBy(msg, Algorithm.DSA.getVal(), signAlgorithm.getVal(), privateKey, cs);
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
        check(keyAlgorithm, signAlgorithm);
        return signBy(msg, keyAlgorithm.getVal(), signAlgorithm.getVal(), privateKey, SecurityEnum.UTF8);
    }

    /**
     * 自定义数字签名
     *
     * @param msg           消息
     * @param keyAlgorithm  密钥算法
     * @param signAlgorithm 签名算法
     * @param privateKey    私钥(base64)
     * @param cs            字符集
     * @return java.lang.String
     */
    public static String signBy(String msg, String keyAlgorithm, String signAlgorithm, String privateKey, Charset cs) {
        cs = Optional.ofNullable(cs).orElse(SecurityEnum.UTF8);
        byte[] privateKeys = Base64Util.decodeToBytes(privateKey);
        byte[] sign = sign(msg.getBytes(cs), keyAlgorithm, signAlgorithm, privateKeys);
        return Base64Util.encode(sign);
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
        check(Algorithm.RSA, signAlgorithm);
        return verifyBy(msg, Algorithm.RSA.getVal(), signAlgorithm.getVal(), publicKey, digitalSignature, cs);
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
        check(Algorithm.DSA, signAlgorithm);
        return verifyBy(msg, Algorithm.DSA.getVal(), signAlgorithm.getVal(), publicKey, digitalSignature, cs);
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
        check(keyAlgorithm, signAlgorithm);
        return verifyBy(msg, keyAlgorithm.getVal(), signAlgorithm.getVal(), publicKey, digitalSignature,
                SecurityEnum.UTF8);
    }

    /**
     * 自定义签名验证
     *
     * @param msg              消息
     * @param keyAlgorithm     密钥算法
     * @param signAlgorithm    签名算法
     * @param publicKey        公钥(base64)
     * @param digitalSignature 数字签名内容(base64)
     * @param cs               字符集
     * @return java.lang.String
     */
    public static boolean verifyBy(String msg, String keyAlgorithm, String signAlgorithm, String publicKey,
            String digitalSignature, Charset cs) {
        byte[] publicKeys = Base64Util.decodeToBytes(publicKey);
        byte[] sign = Base64Util.decodeToBytes(digitalSignature);
        return verify(msg.getBytes(Optional.ofNullable(cs).orElse(SecurityEnum.UTF8)), keyAlgorithm, signAlgorithm,
                publicKeys, sign);
    }

    /**
     * 数字签名
     *
     * @param msg           消息
     * @param keyAlgorithm  密钥算法
     * @param signAlgorithm 签名算法
     * @param privateKeys   私钥
     * @return byte[]
     */
    public static byte[] sign(byte[] msg, String keyAlgorithm, String signAlgorithm, byte[] privateKeys) {
        try {
            // 取得私钥
            KeySpec keySpec = new PKCS8EncodedKeySpec(privateKeys);
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 签名
            Signature signature = Signature.getInstance(signAlgorithm);
            signature.initSign(privateKey);
            signature.update(msg);

            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("DSUtil#sign error.", e);
        }
    }

    /**
     * 验证签名
     *
     * @param msg           消息
     * @param keyAlgorithm  密钥算法
     * @param signAlgorithm 签名算法
     * @param publicKeys    公钥
     * @param sign          数字签名内容
     * @return boolean
     */
    public static boolean verify(byte[] msg, String keyAlgorithm, String signAlgorithm, byte[] publicKeys,
            byte[] sign) {
        try {
            // 取得公钥
            KeySpec keySpec = new X509EncodedKeySpec(publicKeys);
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 签名验证
            Signature signature = Signature.getInstance(signAlgorithm);
            signature.initVerify(publicKey);
            signature.update(msg);

            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException("DSUtil#verify error.", e);
        }
    }

    private static void check(Algorithm keyAlgorithm, Algorithm signAlgorithm) {
        Assert.notNull(keyAlgorithm, "DSUtil#check keyAlgorithm not null.");
        Assert.notNull(signAlgorithm, "DSUtil#check signAlgorithm not null.");

        String keyAlgo = keyAlgorithm.getVal();
        String signAlgo = signAlgorithm.getVal();
        Assert.isTrue(!keyAlgorithm.belong(AbsAlgo.AE), "DSUtil#check invalid keyAlgorithm:" + keyAlgo);
        Assert.isTrue(!signAlgorithm.belong(AbsAlgo.DS), "DSUtil#check invalid signAlgorithm:" + signAlgo);
        Assert.isTrue(!signAlgo.toUpperCase().endsWith(keyAlgo.toUpperCase()),
                "DSUtil#check invalid keyAlgorithm:" + keyAlgo + ",signAlgorithm:" + signAlgo);
    }

    public static void main(String[] args) {
        testDS();
    }

    private static void testDS() {
        String msgStr = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：~";
        String msgStr2 = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：";
        Charset cs = SecurityEnum.UTF8;

        Algorithm keyAlgorithm = Algorithm.DSA;
        Algorithm signAlgorithm = Algorithm.SHA256withDSA;

        Map<String, Key> keyPair = KeyUtil.initKeyPair(keyAlgorithm.getVal(), DSUtil.KEY_SIZE_RSA);
        byte[] publicKeys = KeyUtil.fetchPublicKey(keyPair);
        byte[] privateKeys = KeyUtil.fetchPrivateKey(keyPair);

        String publicKey = Base64Util.encode(publicKeys);
        String privateKey = Base64Util.encode(privateKeys);
        System.out.println(publicKey);
        System.out.println(publicKey.length());
        System.out.println(privateKey);
        System.out.println(privateKey.length());

        byte[] msg = msgStr.getBytes(cs);
        byte[] msg2 = msgStr2.getBytes(cs);

        String signBy = signBy(msgStr, keyAlgorithm, signAlgorithm, privateKey);
        System.out.println(signBy);
        System.out.println(signBy.length());
        System.out.println("验证signBy：" + verifyBy(msgStr, keyAlgorithm, signAlgorithm, publicKey, signBy));

        if (signAlgorithm.getVal().endsWith("RSA")) {
            String signByRSA = signByRSA(msgStr, signAlgorithm, privateKey, cs);
            System.out.println(signByRSA);
            System.out.println(signByRSA.length());
            System.out.println("验证signByRSA：" + verifyByRSA(msgStr, signAlgorithm, publicKey, signBy, cs));
        }

        if (signAlgorithm.getVal().endsWith("DSA")) {
            String signByDSA = signByDSA(msgStr, signAlgorithm, privateKey, cs);
            System.out.println(signByDSA);
            System.out.println(signByDSA.length());
            System.out.println("验证signByDSA：" + verifyByDSA(msgStr, signAlgorithm, publicKey, signBy, cs));
        }

        byte[] sign = sign(msg, keyAlgorithm.getVal(), signAlgorithm.getVal(), privateKeys);
        System.out.println(Base64Util.encode(sign));
        System.out.println("验证sign：" + verify(msg, keyAlgorithm.getVal(), signAlgorithm.getVal(), publicKeys, sign));

        byte[] sign2 = sign(msg, keyAlgorithm.getVal(), signAlgorithm.getVal(), privateKeys);
        System.out.println(Base64Util.encode(sign2));
        System.out.println("验证sign2：" + verify(msg2, keyAlgorithm.getVal(), signAlgorithm.getVal(), publicKeys, sign2));
    }

}
