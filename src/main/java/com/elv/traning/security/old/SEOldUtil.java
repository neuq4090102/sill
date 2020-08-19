package com.elv.traning.security.old;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.elv.core.constant.SecurityEnum;
import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;
import com.elv.core.util.StrUtil;
import com.elv.core.tool.security.KeyUtil;
import com.elv.core.tool.security.NumerationUtil;

/**
 * 对称加密（Symmetric Encryption）
 *
 * @author lxh
 * @since 2020-08-07
 */
public class SEOldUtil {

    /**
     * DES加密
     *
     * @param plaintext 明文
     * @param secretKey 密钥
     * @param iv        密钥
     * @return java.lang.String
     */
    public static String encryptByDES(String plaintext, String secretKey, String iv) {
        return encrypt(plaintext, Algorithm.DES, secretKey, iv, SecurityEnum.UTF8);
    }

    /**
     * DES解密
     *
     * @param hexCiphertext 密文(16进制)
     * @param secretKey     密钥
     * @param iv            初始化向量
     * @return java.lang.String
     */
    public static String decryptByDES(String hexCiphertext, String secretKey, String iv) {
        return decrypt(hexCiphertext, Algorithm.DES, secretKey, iv, SecurityEnum.UTF8);
    }

    /**
     * 3重DES加密
     *
     * @param plaintext 明文
     * @param secretKey 密钥
     * @param iv        密钥
     * @return java.lang.String
     */
    public static String encryptBy3DES(String plaintext, String secretKey, String iv) {
        return encrypt(plaintext, Algorithm.DESede, secretKey, iv, SecurityEnum.UTF8);
    }

    /**
     * 3重DES解密
     *
     * @param hexCiphertext 密文(16进制)
     * @param secretKey     密钥
     * @param iv            初始化向量
     * @return java.lang.String
     */
    public static String decryptBy3DES(String hexCiphertext, String secretKey, String iv) {
        return decrypt(hexCiphertext, Algorithm.DESede, secretKey, iv, SecurityEnum.UTF8);
    }

    /**
     * AES加密
     *
     * @param plaintext 明文
     * @param secretKey 密钥
     * @param iv        初始化向量
     * @return java.lang.String
     */
    public static String encryptByAES(String plaintext, String secretKey, String iv) {
        return encrypt(plaintext, Algorithm.AES, secretKey, iv, SecurityEnum.UTF8);
    }

    /**
     * AES解密
     *
     * @param hexCiphertext 密文(16进制)
     * @param secretKey     密钥
     * @param iv            初始化向量
     * @return java.lang.String
     */
    public static String decryptByAES(String hexCiphertext, String secretKey, String iv) {
        return decrypt(hexCiphertext, Algorithm.AES, secretKey, iv, SecurityEnum.UTF8);
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @param algorithm 密钥算法
     * @param secretKey 密钥
     * @param iv        初始化向量
     * @param cs        字符集
     * @return java.lang.String
     */
    public static String encrypt(String plaintext, Algorithm algorithm, String secretKey, String iv, Charset cs) {
        Assert.notBlank(plaintext, "SEUtil#encrypt plaintext not blank.");
        Assert.notBlank(secretKey, "SEUtil#encrypt secretKey not blank.");
        Assert.notNull(algorithm, "SEUtil#encrypt algorithm not null.");
        Assert.isTrue(!algorithm.belong(AbsAlgo.SE), "SEUtil#encrypt invalid algorithm:" + algorithm.getVal());

        cs = Optional.ofNullable(cs).orElse(SecurityEnum.UTF8);

        byte[] ivBytes = null;
        if (StrUtil.isNotBlank(iv)) {
            ivBytes = iv.getBytes(cs);
        }
        byte[] bytes = encrypt(plaintext.getBytes(cs), algorithm.getVal(), algorithm.getCipher(),
                secretKey.getBytes(cs), ivBytes);
        return NumerationUtil.toHex(bytes);
    }

    /**
     * 解密
     *
     * @param hexCiphertext 密文(16进制)
     * @param algorithm     密钥算法
     * @param secretKey     密钥
     * @param iv            初始化向量
     * @param cs            字符集
     * @return java.lang.String
     */
    public static String decrypt(String hexCiphertext, Algorithm algorithm, String secretKey, String iv, Charset cs) {
        Assert.notBlank(hexCiphertext, "SEUtil#encrypt hexCiphertext not blank.");
        Assert.notBlank(secretKey, "SEUtil#encrypt secretKey not blank.");
        Assert.notNull(algorithm, "SEUtil#decrypt algorithm not null.");
        Assert.isTrue(!algorithm.belong(AbsAlgo.SE), "SEUtil#decrypt invalid algorithm:" + algorithm.getVal());

        cs = Optional.ofNullable(cs).orElse(SecurityEnum.UTF8);

        byte[] ivs = null;
        if (StrUtil.isNotBlank(iv)) {
            ivs = iv.getBytes(cs);
        }

        byte[] ciphertext = NumerationUtil.toBytes(hexCiphertext);
        byte[] bytes = decrypt(ciphertext, algorithm.getVal(), algorithm.getCipher(), secretKey.getBytes(cs), ivs);
        return new String(bytes, cs);
    }

    /**
     * 加密
     *
     * @param plaintext       明文
     * @param algorithm       密钥算法
     * @param cipherAlgorithm 加密算法
     * @param secretKey       密钥
     * @param iv              初始化向量
     * @return byte[]
     */
    public static byte[] encrypt(byte[] plaintext, String algorithm, String cipherAlgorithm, byte[] secretKey,
            byte[] iv) {
        try {
            Key key = initKey(algorithm, secretKey);
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            if (iv == null || iv.length == 0) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            }
            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            throw new RuntimeException("SEUtil#encrypt error.", e);
        }
    }

    /**
     * 解密
     *
     * @param ciphertext      密文
     * @param algorithm       密钥算法
     * @param cipherAlgorithm 解密算法
     * @param secretKey       密钥
     * @param iv              初始化向量
     * @return byte[]
     */
    public static byte[] decrypt(byte[] ciphertext, String algorithm, String cipherAlgorithm, byte[] secretKey,
            byte[] iv) {
        try {
            Key key = initKey(algorithm, secretKey);
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            if (iv == null || iv.length == 0) {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            }
            return cipher.doFinal(ciphertext);
        } catch (Exception e) {
            throw new RuntimeException("SEUtil#decrypt error.", e);
        }
    }

    /**
     * 生成密钥
     *
     * @param algorithm 密钥算法
     * @param secretKey 密钥
     * @return java.security.Key
     */
    private static Key initKey(String algorithm, byte[] secretKey) throws Exception {
        if (Algorithm.DES.getVal().equalsIgnoreCase(algorithm)) {
            return KeyUtil.initSecretKey(algorithm, new DESKeySpec(secretKey));
        } else if (Algorithm.DESede.getVal().equalsIgnoreCase(algorithm)) {
            return KeyUtil.initSecretKey(algorithm, new DESedeKeySpec(secretKey));
        }
        return new SecretKeySpec(secretKey, algorithm);
    }

    public static void main(String[] args) {
        testSE();
    }

    private static void testSE() {
        String plaintext = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：";
        String secretKey = "c4680140a338cdcf"; // 密钥
        String iv = "4cb79d35"; // 初始化向量(根据工作模式，有的不需要)
        String desCipher = SEOldUtil.encryptByDES(plaintext, secretKey, iv);
        System.out.println(desCipher);
        System.out.println(SEOldUtil.decryptByDES(desCipher, secretKey, iv));

        String desEdeCipher = SEOldUtil.encryptBy3DES(plaintext, secretKey + secretKey + secretKey, iv);
        System.out.println(desEdeCipher);
        System.out.println(SEOldUtil.decryptBy3DES(desEdeCipher, secretKey + secretKey + secretKey, iv));

        String aesCipher = SEOldUtil.encryptByAES(plaintext, secretKey, "4cb79d35583f8ea6");
        System.out.println(aesCipher);
        System.out.println(SEOldUtil.decryptByAES(aesCipher, secretKey, "4cb79d35583f8ea6"));
    }
}
