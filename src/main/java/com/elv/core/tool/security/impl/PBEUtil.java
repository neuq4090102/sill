package com.elv.core.tool.security.impl;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;
import com.elv.core.tool.security.AbstractSEUtil;
import com.elv.core.tool.security.KeyUtil;
import com.elv.core.tool.security.NumerationUtil;

/**
 * desc
 *
 * @author lxh
 * @since 2020-08-19
 */
public class PBEUtil extends AbstractSEUtil {

    private String algorithm; // 密钥算法
    private String cipherAlgorithm; // 加密/解密算法
    private String password; // 密码
    private String salt; // 盐
    private int iterationCount = 100; // 迭代次数

    private PBEUtil() {
    }

    public static PBEUtil of() {
        return new PBEUtil();
    }

    @Override
    public String getCipherAlgorithm() {
        return cipherAlgorithm;
    }

    @Override
    public Key getKey() {
        return KeyUtil.initSecretKey(algorithm, new PBEKeySpec(password.toCharArray()));
    }

    @Override
    public AlgorithmParameterSpec getSpec() {
        return new PBEParameterSpec(salt.getBytes(cs), iterationCount);
    }

    public PBEUtil algorithm(Algorithm algorithm) {
        Assert.isTrue(!algorithm.belong(AbsAlgo.SE) || !algorithm.getVal().toUpperCase().startsWith("PBE"),
                "PBEUtil#algorithm invalid algorithm:" + algorithm.getVal());
        this.algorithm = algorithm.getVal();
        this.cipherAlgorithm = algorithm.getCipher();
        return this;
    }

    public PBEUtil algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public PBEUtil cipherAlgorithm(String cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
        return this;
    }

    public PBEUtil salt(String salt) {
        this.salt = salt;
        return this;
    }

    public PBEUtil iterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
        return this;
    }

    public PBEUtil cs(Charset cs) {
        this.cs = cs;
        return this;
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @param password  口令
     * @return java.lang.String
     */
    public String encrypt(String plaintext, String password) {
        checkAndInit(plaintext, password);
        return NumerationUtil.toHex(encrypt(plaintext.getBytes(cs)));
    }

    /**
     * 解密
     *
     * @param hexCipherText 密文(16进制)
     * @param password      口令
     * @return java.lang.String
     */
    public String decrypt(String hexCipherText, String password) {
        checkAndInit(hexCipherText, password);
        return new String(decrypt(NumerationUtil.toBytes(hexCipherText)), cs);
    }

    private void checkAndInit(String msg, String password) {
        Assert.notBlank("PBEUtil#checkAndInit param not blank.", msg, password, algorithm, cipherAlgorithm, salt);
        Assert.isTrue(salt.length() != 8, "PBEUtil#checkAndInit salt's value must be 8 bytes long.");
        this.password = password;

        init();
    }

    public static void main(String[] args) {
        testPBE();
    }

    private static void testPBE() {
        String plaintext = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：";
        String salt = "c4680140"; // 密钥
        int iterationCount = 10;

        String encrypt = PBEUtil.of().algorithm(Algorithm.PBEWithMD5AndDES).salt(salt).iterationCount(iterationCount)
                .encrypt(plaintext, "123");
        System.out.println(encrypt);
        System.out.println(PBEUtil.of().algorithm(Algorithm.PBEWithMD5AndDES).salt(salt).iterationCount(iterationCount)
                .decrypt(encrypt, "123"));
    }
}
