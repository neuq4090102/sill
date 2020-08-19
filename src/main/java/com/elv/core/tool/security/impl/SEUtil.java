package com.elv.core.tool.security.impl;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;
import com.elv.core.util.StrUtil;
import com.elv.core.tool.security.AbstractSEUtil;
import com.elv.core.tool.security.KeyUtil;
import com.elv.core.tool.security.NumerationUtil;

/**
 * 对称加密工具
 *
 * @author lxh
 * @since 2020-08-07
 */
public class SEUtil extends AbstractSEUtil {

    private String algorithm; // 密钥算法
    private String cipherAlgorithm; // 加密/解密算法
    private String secretKey; // 密钥
    private String iv; // 初始化向量

    private SEUtil() {
    }

    public static SEUtil of() {
        return new SEUtil();
    }

    @Override
    public String getCipherAlgorithm() {
        return cipherAlgorithm;
    }

    @Override
    public Key getKey() {
        return fetchKey(algorithm, secretKey.getBytes(cs));
    }

    @Override
    public AlgorithmParameterSpec getSpec() {
        if (StrUtil.isNotBlank(iv)) {
            return new IvParameterSpec(iv.getBytes(cs));
        }
        return super.getSpec();
    }

    public SEUtil algorithm(Algorithm algorithm) {
        Assert.isTrue(!algorithm.belong(AbsAlgo.SE), "SEUtil#algorithm invalid algorithm:" + algorithm.getVal());

        this.algorithm = algorithm.getVal();
        this.cipherAlgorithm = algorithm.getCipher();
        return this;
    }

    public SEUtil algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public SEUtil cipherAlgorithm(String cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
        return this;
    }

    public SEUtil secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public SEUtil iv(String iv) {
        this.iv = iv;
        return this;
    }

    public SEUtil cs(Charset cs) {
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
        checkAndInit(plaintext);
        return NumerationUtil.toHex(encrypt(plaintext.getBytes(cs)));
    }

    /**
     * 解密
     *
     * @param hexCipherText 密文(16进制)
     * @return java.lang.String
     */
    public String decrypt(String hexCipherText) {
        checkAndInit(hexCipherText);
        return new String(decrypt(NumerationUtil.toBytes(hexCipherText)), cs);
    }

    private void checkAndInit(String msg) {
        Assert.notBlank("SEUtil#checkAndInit param not blank.", msg, algorithm, cipherAlgorithm, secretKey);
        init();
    }

    /**
     * 生成密钥
     *
     * @param algorithm 密钥算法
     * @param secretKey 密钥
     * @return java.security.Key
     */
    private Key fetchKey(String algorithm, byte[] secretKey) {
        try {
            if (Algorithm.DES.getVal().equalsIgnoreCase(algorithm)) {
                return KeyUtil.initSecretKey(algorithm, new DESKeySpec(secretKey));
            } else if (Algorithm.DESede.getVal().equalsIgnoreCase(algorithm)) {
                return KeyUtil.initSecretKey(algorithm, new DESedeKeySpec(secretKey));
            }
            return new SecretKeySpec(secretKey, algorithm);
        } catch (Exception e) {
            throw new RuntimeException("SEUtil#initKey error.", e);
        }
    }

    public static void main(String[] args) {
        testSE();
    }

    private static void testSE() {
        String plaintext = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：";
        String secretKey = "c4680140a338cdcf"; // 密钥
        String iv = "4cb79d35"; // 初始化向量(根据工作模式，有的不需要)

        String encrypt = SEUtil.of().algorithm(Algorithm.DES).secretKey(secretKey).iv(iv).encrypt(plaintext);
        System.out.println(encrypt);
        System.out.println(SEUtil.of().algorithm(Algorithm.DES).secretKey(secretKey).iv(iv).decrypt(encrypt));
    }
}
