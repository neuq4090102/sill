package com.elv.core.tool.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 非对称加密（Asymmetric encryption）
 *
 * @author lxh
 * @since 2020-08-18
 */
public abstract class AbstractAEUtil extends BasicSecurity {

    /**
     * 密钥算法
     *
     * @return java.lang.String
     */
    public abstract String getAlgorithm();

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
     * 公钥加密
     *
     * @param plaintext 明文
     * @return byte[]
     */
    public byte[] encrypt(byte[] plaintext) {
        try {
            // 取得公钥
            KeySpec keySpec = new X509EncodedKeySpec(getPublicKeys());
            KeyFactory keyFactory = KeyFactory.getInstance(getAlgorithm());
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 公钥加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plaintext);

        } catch (Exception e) {
            throw new RuntimeException("AbstractAEUtil#encrypt error.", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param cipherText 密文
     * @return byte[]
     */
    public byte[] decrypt(byte[] cipherText) {
        try {
            // 取得私钥
            KeySpec keySpec = new PKCS8EncodedKeySpec(getPrivateKeys());
            KeyFactory keyFactory = KeyFactory.getInstance(getAlgorithm());
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 公钥加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("AbstractAEUtil#decrypt error.", e);
        }
    }
}
