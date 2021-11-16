package com.elv.core.tool.security.impl;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Map;

import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.tool.security.AbstractDSUtil;
import com.elv.core.tool.security.Base64Util;
import com.elv.core.tool.security.KeyUtil;
import com.elv.core.util.Assert;
import com.elv.core.util.Dater;

/**
 * 数字签名
 *
 * @author lxh
 * @since 2020-08-18
 */
public class DSUtil extends AbstractDSUtil {

    public static final int KEY_SIZE_RSA = 2048;

    private String signAlgorithm; // 签名算法
    private String keyAlgorithm; // 密钥算法
    private String publicKey; // 公钥(base64)
    private String privateKey; // 私钥(base64)

    private DSUtil() {
    }

    public static DSUtil of() {
        return new DSUtil();
    }

    @Override
    public String getSignAlgorithm() {
        return signAlgorithm;
    }

    @Override
    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    @Override
    public byte[] getPublicKeys() {
        return Base64Util.decodeToBytes(publicKey);
    }

    @Override
    public byte[] getPrivateKeys() {
        return Base64Util.decodeToBytes(privateKey);
    }

    public DSUtil signAlgorithm(Algorithm signAlgorithm) {
        Assert.isTrue(!signAlgorithm.belong(AbsAlgo.DS),
                "DSUtil#signAlgorithm invalid algorithm:" + signAlgorithm.getVal());
        this.signAlgorithm = signAlgorithm.getVal();
        return this;
    }

    public DSUtil signAlgorithm(String signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
        return this;
    }

    public DSUtil keyAlgorithm(Algorithm keyAlgorithm) {
        Assert.isTrue(!keyAlgorithm.belong(AbsAlgo.AE),
                "DSUtil#keyAlgorithm invalid algorithm:" + keyAlgorithm.getVal());
        this.keyAlgorithm = keyAlgorithm.getVal();
        return this;
    }

    public DSUtil keyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
        return this;
    }

    public DSUtil publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public DSUtil privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public DSUtil cs(Charset cs) {
        this.cs = cs;
        return this;
    }

    /**
     * 签名
     *
     * @param msg 消息
     * @return java.lang.String
     */
    public String sign(String msg) {
        Assert.notBlank("DSUtil#sign param not blank.", msg, keyAlgorithm, signAlgorithm, privateKey);
        Assert.isTrue(!signAlgorithm.toUpperCase().endsWith(keyAlgorithm.toUpperCase()),
                "DSUtil#sign invalid keyAlgorithm:" + keyAlgorithm + ",signAlgorithm:" + signAlgorithm);
        init();
        return Base64Util.encode(sign(msg.getBytes(cs)));
    }

    /**
     * 签名验证
     *
     * @param msg              消息
     * @param digitalSignature 数字签名内容(base64)
     * @return boolean
     */
    public boolean verify(String msg, String digitalSignature) {
        Assert.notBlank("DSUtil#verify param not blank.", msg, digitalSignature, keyAlgorithm, signAlgorithm,
                publicKey);
        Assert.isTrue(!signAlgorithm.toUpperCase().endsWith(keyAlgorithm.toUpperCase()),
                "DSUtil#verify invalid keyAlgorithm:" + keyAlgorithm + ",signAlgorithm:" + signAlgorithm);

        return verify(msg.getBytes(cs), Base64Util.decodeToBytes(digitalSignature));
    }

    public static void main(String[] args) {
        testDS();
    }

    private static void testDS() {
        String msgStr = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：~";
        String msgStr2 = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：";

        // Algorithm keyAlgorithm = Algorithm.DSA;
        // Algorithm signAlgorithm = Algorithm.SHA256withDSA;
        Algorithm keyAlgorithm = Algorithm.RSA;
        Algorithm signAlgorithm = Algorithm.SHA256withRSA;

        Map<String, Key> keyPair = KeyUtil.initKeyPair(keyAlgorithm.getVal(), KEY_SIZE_RSA);
        byte[] publicKeys = KeyUtil.fetchPublicKey(keyPair);
        byte[] privateKeys = KeyUtil.fetchPrivateKey(keyPair);

        String publicKey = Base64Util.encode(publicKeys);
        String privateKey = Base64Util.encode(privateKeys);
        System.out.println(publicKey);
        System.out.println(publicKey.length());
        System.out.println(privateKey);
        System.out.println(privateKey.length());

        long t1 = Dater.now().ts();
        String signBy = DSUtil.of().signAlgorithm(signAlgorithm).keyAlgorithm(keyAlgorithm).privateKey(privateKey)
                .sign(msgStr);
        System.out.println("time:" + (Dater.now().ts() - t1));
        System.out.println(signBy);
        System.out.println(signBy.length());
        System.out.println(
                "验证signBy：" + DSUtil.of().signAlgorithm(signAlgorithm).keyAlgorithm(keyAlgorithm).publicKey(publicKey)
                        .verify(msgStr, signBy));

        String sign2 = DSUtil.of().signAlgorithm(signAlgorithm).keyAlgorithm(keyAlgorithm).privateKey(privateKey)
                .sign(msgStr2);
        System.out.println(sign2);
        System.out.println(
                "验证sign2：" + DSUtil.of().signAlgorithm(signAlgorithm).keyAlgorithm(keyAlgorithm).publicKey(publicKey)
                        .verify(msgStr, sign2));
    }
}
