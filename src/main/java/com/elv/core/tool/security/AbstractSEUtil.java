package com.elv.core.tool.security;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;

/**
 * 对称加密（Symmetric Encryption）
 *
 * @author lxh
 * @since 2020-08-07
 */
public abstract class AbstractSEUtil extends BasicSecurity {

    /**
     * 加密/解密算法
     *
     * @return java.lang.String
     */
    public abstract String getCipherAlgorithm();

    /**
     * 密钥
     *
     * @return java.security.Key
     */
    public abstract Key getKey();

    /**
     * 密钥规范（IV/PBE等）
     *
     * @return java.security.spec.AlgorithmParameterSpec
     */
    public AlgorithmParameterSpec getSpec() {
        return null;
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @return byte[]
     */
    public byte[] encrypt(byte[] plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(getCipherAlgorithm());
            AlgorithmParameterSpec spec = getSpec();
            if (spec != null) {
                cipher.init(Cipher.ENCRYPT_MODE, getKey(), spec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, getKey());
            }
            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            throw new RuntimeException("AbstractSEUtil#encrypt error.", e);
        }
    }

    /**
     * 解密
     *
     * @param cipherText 密文
     * @return byte[]
     */
    public byte[] decrypt(byte[] cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(getCipherAlgorithm());

            AlgorithmParameterSpec spec = getSpec();
            if (spec != null) {
                cipher.init(Cipher.DECRYPT_MODE, getKey(), spec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, getKey());
            }
            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("AbstractSEUtil#decrypt error.", e);
        }
    }

}
