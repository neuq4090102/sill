package com.elv.traning.security;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;

/**
 * 密钥工具
 * <p>
 * KeyGenerator：密钥生成器，随机生成一个密钥，常用于不可逆算法中，如MD
 * SecretKeyFactory（SecretKey）：密钥工厂，常用于对称加密算法中，如DES，PBE
 * KeyPairGenerator：密钥对生成器，生成一对密钥，即公钥和私钥，常用于非对称加密算法中，如RSA
 *
 * @author lxh
 * @since 2020-08-07
 */
public class KeyUtil {

    private static final String PUBLIC_KEY = "PUBLIC_KEY";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";

    /**
     * 密钥生成
     *
     * @param algorithm 密钥算法
     * @return java.security.Key
     * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyGenerator">密钥算法</a>
     */
    public static Key initKey(String algorithm) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("KeyUtil#initKey error.", e);
        }
    }

    /**
     * 密钥生成
     *
     * @param algorithm 密钥算法
     * @param keySpec   密钥规范
     * @return java.security.Key
     * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory">密钥算法</a>
     */
    public static Key initSecretKey(String algorithm, KeySpec keySpec) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            return factory.generateSecret(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("KeyUtil#initSecretKey error.", e);
        }
    }

    /**
     * 密钥对生成
     *
     * @param algorithm 密钥对算法
     * @param keySize   密钥长度(64的倍数，最小值512)
     * @return java.util.Map
     * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">密钥对算法</a>
     */
    public static Map<String, Key> initKeyPair(String algorithm, int keySize) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
            generator.initialize(keySize);

            KeyPair keyPair = generator.generateKeyPair();
            keyPair.getPublic();

            Map<String, Key> map = new HashMap<>();
            map.put(PUBLIC_KEY, keyPair.getPublic());
            map.put(PRIVATE_KEY, keyPair.getPrivate());

            return map;
        } catch (Exception e) {
            throw new RuntimeException("KeyUtil#initKeyPair error.", e);
        }
    }

    /**
     * 获取公钥
     *
     * @param map 密钥对
     * @return byte[]
     */
    public static byte[] fetchPublicKey(Map<String, Key> map) {
        return map.get(PUBLIC_KEY).getEncoded();
    }

    /**
     * 获取私钥
     *
     * @param map 密钥对
     * @return byte[]
     */
    public static byte[] fetchPrivateKey(Map<String, Key> map) {
        return map.get(PRIVATE_KEY).getEncoded();
    }
}
