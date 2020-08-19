package com.elv.core.tool.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 数字签名（Digital Signature）
 * <p>
 * 可看作是公钥加密的逆过程
 *
 * @author lxh
 * @since 2020-08-07
 */
public abstract class AbstractDSUtil extends BasicSecurity {

    /**
     * 签名算法
     *
     * @return java.lang.String
     */
    public abstract String getSignAlgorithm();

    /**
     * 密钥算法
     *
     * @return java.lang.String
     */
    public abstract String getKeyAlgorithm();

    /**
     * 公钥
     *
     * @return byte[]
     */
    public abstract byte[] getPublicKeys();

    /**
     * 私钥
     *
     * @return byte[]
     */
    public abstract byte[] getPrivateKeys();

    /**
     * 数字签名
     *
     * @param msg 消息
     * @return byte[]
     */
    public byte[] sign(byte[] msg) {
        try {
            // 取得私钥
            KeySpec keySpec = new PKCS8EncodedKeySpec(getPrivateKeys());
            KeyFactory keyFactory = KeyFactory.getInstance(getKeyAlgorithm());
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 签名
            Signature signature = Signature.getInstance(getSignAlgorithm());
            signature.initSign(privateKey);
            signature.update(msg);

            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("AbstractDSUtil#sign error.", e);
        }
    }

    /**
     * 验证签名
     *
     * @param msg  消息
     * @param sign 数字签名内容
     * @return boolean
     */
    public boolean verify(byte[] msg, byte[] sign) {
        try {
            // 取得公钥
            KeySpec keySpec = new X509EncodedKeySpec(getPublicKeys());
            KeyFactory keyFactory = KeyFactory.getInstance(getKeyAlgorithm());
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 签名验证
            Signature signature = Signature.getInstance(getSignAlgorithm());
            signature.initVerify(publicKey);
            signature.update(msg);

            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException("AbstractDSUtil#verify error.", e);
        }
    }
}
