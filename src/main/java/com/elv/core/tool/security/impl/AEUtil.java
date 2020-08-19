package com.elv.core.tool.security.impl;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Map;

import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;
import com.elv.core.tool.security.AbstractAEUtil;
import com.elv.core.tool.security.Base64Util;
import com.elv.core.tool.security.KeyUtil;

/**
 * 非对称加密工具
 *
 * @author lxh
 * @since 2020-08-07
 */
public class AEUtil extends AbstractAEUtil {

    public static final int KEY_SIZE_RSA = 2048;

    private String algorithm; // 密钥算法
    private String publicKey; // 公钥(base64)
    private String privateKey; // 私钥(base64)

    private AEUtil() {
    }

    public static AEUtil of() {
        return new AEUtil();
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public byte[] getPublicKeys() {
        return Base64Util.decodeToBytes(publicKey);
    }

    @Override
    public byte[] getPrivateKeys() {
        return Base64Util.decodeToBytes(privateKey);
    }

    public AEUtil algorithm(Algorithm algorithm) {
        Assert.isTrue(!algorithm.belong(AbsAlgo.AE), "AEUtil#algorithm invalid algorithm:" + algorithm.getVal());
        this.algorithm = algorithm.getVal();
        return this;
    }

    public AEUtil algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public AEUtil publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public AEUtil privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public AEUtil cs(Charset cs) {
        this.cs = cs;
        return this;
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @return java.lang.String
     */
    public String encrypt(String plaintext) {
        Assert.notBlank("AEUtil#encrypt param not blank.", plaintext, algorithm, publicKey);
        init();
        return Base64Util.base64Encode(encrypt(plaintext.getBytes(cs)));

    }

    /**
     * 解密
     *
     * @param cipherText 密文(base64)
     * @return java.lang.String
     */
    public String decrypt(String cipherText) {
        Assert.notBlank("AEUtil#decrypt param not blank.", cipherText, algorithm, privateKey);
        init();
        return new String(decrypt(Base64Util.decodeToBytes(cipherText)), cs);
    }

    public static void main(String[] args) {
        testAE();
    }

    private static void testAE() {
        String plaintext = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：~";
        Map<String, Key> keyPair = AEUtil.initRsaKeyPair(KEY_SIZE_RSA);
        byte[] publicKeys = KeyUtil.fetchPublicKey(keyPair);
        byte[] privateKeys = KeyUtil.fetchPrivateKey(keyPair);

        // base64
        String publicKey = Base64Util.encode(publicKeys);
        String privateKey = Base64Util.encode(privateKeys);
        System.out.println(publicKey);
        System.out.println(publicKey.length());
        System.out.println(privateKey);
        System.out.println(privateKey.length());

        String encryptByRsa = AEUtil.of().algorithm(Algorithm.RSA).publicKey(publicKey).encrypt(plaintext);
        System.out.println(encryptByRsa);
        System.out.println(AEUtil.of().algorithm(Algorithm.RSA).privateKey(privateKey).decrypt(encryptByRsa));
    }

    private static Map<String, Key> initRsaKeyPair(int keySize) {
        return KeyUtil.initKeyPair(Algorithm.RSA.getVal(), keySize);
    }

}
