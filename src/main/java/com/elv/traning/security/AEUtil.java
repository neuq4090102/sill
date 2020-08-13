package com.elv.traning.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.Cipher;

import com.elv.core.constant.SecurityEnum;
import com.elv.core.constant.SecurityEnum.Algorithm;

/**
 * 非对称加密（Asymmetric encryption）
 *
 * @author lxh
 * @since 2020-08-07
 */
public class AEUtil {

    public static final int KEY_SIZE_RSA = 2048;

    /**
     * RSA加密
     *
     * @param plaintext  明文
     * @param publicKeys 公钥
     * @return byte[]
     */
    public static byte[] encryptByRSA(String plaintext, byte[] publicKeys) {
        return encrypt(plaintext.getBytes(SecurityEnum.UTF8), Algorithm.RSA.getVal(), publicKeys);
    }

    /**
     * RSA解密
     *
     * @param ciphers     密文
     * @param privateKeys 私钥
     * @return byte[]
     */
    public static byte[] decryptByRSA(byte[] ciphers, byte[] privateKeys) {
        return decrypt(ciphers, Algorithm.RSA.getVal(), privateKeys);
    }

    /**
     * RSA加密
     *
     * @param plaintext    明文
     * @param publicKeyStr 公钥(base64)
     * @return java.lang.String
     */
    public static String encryptByRSA(String plaintext, String publicKeyStr) {
        byte[] bytes = encrypt(plaintext.getBytes(SecurityEnum.UTF8), Algorithm.RSA.getVal(),
                Base64Util.decodeToBytes(publicKeyStr));
        return Base64Util.base64Encode(bytes);
    }

    /**
     * RSA解密
     *
     * @param ciphertext    密文(base64)
     * @param privateKeyStr 私钥(base64)
     * @return java.lang.String
     */
    public static String decryptByRSA(String ciphertext, String privateKeyStr) {
        byte[] bytes = decrypt(Base64Util.decodeToBytes(ciphertext), Algorithm.RSA.getVal(),
                Base64Util.decodeToBytes(privateKeyStr));
        return new String(bytes, SecurityEnum.UTF8);
    }

    /**
     * 公钥加密
     *
     * @param plaintext  明文
     * @param algorithm  算法
     * @param publicKeys 公钥
     * @return byte[]
     */
    public static byte[] encrypt(byte[] plaintext, String algorithm, byte[] publicKeys) {
        try {
            // 取得公钥
            KeySpec keySpec = new X509EncodedKeySpec(publicKeys);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 公钥加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plaintext);

        } catch (Exception e) {
            throw new RuntimeException("AE#encrypt error.", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param ciphers     密文
     * @param algorithm   算法
     * @param privateKeys 私钥
     * @return byte[]
     */
    public static byte[] decrypt(byte[] ciphers, String algorithm, byte[] privateKeys) {
        try {
            // 取得私钥
            KeySpec keySpec = new PKCS8EncodedKeySpec(privateKeys);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 公钥加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(ciphers);
        } catch (Exception e) {
            throw new RuntimeException("AE#decrypt error.", e);
        }
    }

    public static void main(String[] args) {
        testAE();
    }

    private static void testAE() {
        String plaintext = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：~";
        Map<String, Key> keyPair = AEUtil.initRsaKeyPair(AEUtil.KEY_SIZE_RSA);
        byte[] publicKeys = KeyUtil.fetchPublicKey(keyPair);
        byte[] privateKeys = KeyUtil.fetchPrivateKey(keyPair);

        // 公钥加密
        byte[] ciphers = AEUtil.encryptByRSA(plaintext, publicKeys);
        String cipherText = Base64Util.base64Encode(ciphers);
        System.out.println("cipher text:" + cipherText);
        System.out.println("cipher length:" + cipherText.length());

        // 私钥解密
        byte[] dataBytes = AEUtil.decryptByRSA(ciphers, privateKeys);
        System.out.println(new String(dataBytes, SecurityEnum.UTF8));

        // base64
        String publicKeyStr = Base64Util.encode(publicKeys);
        String privateKeyStr = Base64Util.encode(privateKeys);
        System.out.println(publicKeyStr);
        System.out.println(publicKeyStr.length());
        System.out.println(privateKeyStr);
        System.out.println(privateKeyStr.length());

        String encryptByRsa = encryptByRSA(plaintext, publicKeyStr);
        System.out.println(encryptByRsa);
        System.out.println(decryptByRSA(encryptByRsa, privateKeyStr));
    }

    private static Map<String, Key> initRsaKeyPair(int keySize) {
        return KeyUtil.initKeyPair(Algorithm.RSA.getVal(), keySize);
    }

}
