package com.elv.traning.security;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.elv.core.util.Assert;

/**
 * @author lxh
 * @since 2020-06-16
 */
public class Security {

    private static final Charset UTF8;
    private static final SecureRandom RANDOM;

    static {
        UTF8 = StandardCharsets.UTF_8;
        RANDOM = new SecureRandom();
    }

    private Security() {
    }

    public static String base64Encode(String str) {
        return BASE64.encode(str, UTF8);
    }

    public static String base64Decode(String str) {
        return BASE64.decode(str, UTF8);
    }

    public static String base64Encode(String str, Charset cs) {
        return BASE64.encode(str, cs);
    }

    public static String base64Decode(String str, Charset cs) {
        return BASE64.decode(str, cs);
    }

    public static String base64Encode(byte[] bytes) {
        return BASE64.encode(bytes);
    }

    public static byte[] base64DecodeToBytes(String base64Str) {
        return BASE64.decode(base64Str);
    }

    public static String md5(String str) {
        return mdToHex(Algorithm.MD5.getVal(), str);
    }

    public static String md5(String str, Charset cs) {
        return mdToHex(Algorithm.MD5.getVal(), str, cs);
    }

    public static String sha(String str) {
        return mdToHex(Algorithm.SHA.getVal(), str);
    }

    public static String sha(String str, Charset cs) {
        return mdToHex(Algorithm.SHA.getVal(), str, cs);
    }

    public static String sha256(String str) {
        return mdToHex(Algorithm.SHA256.getVal(), str);
    }

    public static String sha512(String str) {
        return mdToHex(Algorithm.SHA512.getVal(), str);
    }

    public static String mdBy(String str, Charset cs, String algorithm) {
        return mdToHex(algorithm, str, cs);
    }

    /**
     * @param str           签名数据
     * @param cs            字符集
     * @param algorithm     签名算法
     * @param privateKeyStr 私钥字符串
     * @return RSA签名结果
     */
    public static String sighByRSA(String str, Charset cs, Algorithm algorithm, String privateKeyStr) {
        Assert.isTrue(algorithm.getAlgo() != AbsAlgo.SIG, algorithm.getVal() + " doesn't support signature algorithm.");
        return signByRSAWith(str, cs, algorithm.getVal(), privateKeyStr);
    }

    public static String signByRSAWith(String str, Charset cs, String signAlgorithm, String privateKeyStr) {
        return base64Encode(SIG.sign(str, cs, Algorithm.RSA.getVal(), signAlgorithm, privateKeyStr));
    }

    public static String sighByDSA(String str, Charset cs, Algorithm algorithm, String privateKeyStr) {
        Assert.isTrue(algorithm.getAlgo() != AbsAlgo.SIG, algorithm.getVal() + " doesn't support signature algorithm.");
        return signByDSAWith(str, cs, algorithm.getVal(), privateKeyStr);
    }

    public static String signByDSAWith(String str, Charset cs, String signAlgorithm, String privateKeyStr) {
        return base64Encode(SIG.sign(str, cs, Algorithm.DSA.getVal(), signAlgorithm, privateKeyStr));
    }

    /**
     * 生成16进制字符串
     *
     * @param bytes 字节数组
     * @return java.lang.String
     * @author lxh
     * @since 2020-07-30
     */
    public static String toHex(byte[] bytes) {
        return NumerationUtil.toHex(bytes);
    }

    /**
     * 解析16进制内容
     *
     * @param hexStr 16进制字符串
     * @param cs     字符集
     * @return java.lang.String
     * @see #toHex
     */
    public static String hexToStr(String hexStr, Charset cs) {
        return new String(NumerationUtil.toBytes(hexStr), cs);
    }

    /**
     * 盐值生成
     *
     * @return java.lang.String
     */
    public static String salt() {
        return NumerationUtil.toHex(RANDOM.generateSeed(8));
    }

    // ******************************** base64-begin ********************************* //

    private static class BASE64 {
        public static String encode(String str, Charset cs) {
            return Base64.getEncoder().encodeToString(str.getBytes(cs));
        }

        public static String decode(String str, Charset cs) {
            return new String(Base64.getDecoder().decode(str), cs);
        }

        public static String encode(byte[] bytes) {
            return Base64.getEncoder().encodeToString(bytes);
        }

        public static byte[] decode(String base64Str) {
            return Base64.getDecoder().decode(base64Str);
        }
    }

    // ******************************** base64-end ********************************* //

    @FunctionalInterface
    interface MD {
        byte[] md(String algorithm, String str, Charset charset);

        default byte[] md(String algorithm, String str) {
            return md(algorithm, str, UTF8);
        }

        default String toHex(String algorithm, String str, Charset charset) {
            return NumerationUtil.toHex(md(algorithm, str, charset));
        }
    }

    private static MD getMD() {
        return (String algorithm, String str, Charset charset) -> {
            try {
                return MessageDigest.getInstance(algorithm).digest(str.getBytes(charset));
            } catch (Exception e) {
                throw new RuntimeException("MD algorithm [" + algorithm + "] not found.");
            }
        };
    }

    private static String mdToHex(String algorithm, String str, Charset charset) {
        return getMD().toHex(algorithm, str, charset);
    }

    private static String mdToHex(String algorithm, String str) {
        return getMD().toHex(algorithm, str, UTF8);
    }

    private static class MAC {
        public static String init(String algorithm) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
                return toHex(keyGenerator.generateKey().getEncoded());
            } catch (Exception e) {
                throw new RuntimeException("MAC algorithm [" + algorithm + "] not found.");
            }
        }

        public static String encode(String data, String key, Algorithm algorithm) {
            try {
                SecretKey secretKey = new SecretKeySpec(key.getBytes(), algorithm.getVal());
                Mac mac = Mac.getInstance(secretKey.getAlgorithm());
                mac.init(secretKey);
                return toHex(mac.doFinal(data.getBytes(UTF8)));
            } catch (Exception e) {
                throw new RuntimeException("MAC algorithm [" + algorithm + "] not found.");
            }
        }
    }

    /**
     * 对称加密（Symmetric Encryption）
     */
    private static class SE {

        /**
         * @param secret  对称密钥
         * @param charset 字符集
         * @return
         */
        private static Key toKey(Algorithm algorithm, String secret, Charset charset) throws Exception {
            if (algorithm.getAlgo() != AbsAlgo.SE) {
                throw new RuntimeException("SE toKey invalid algorithm.");
            }
            if (algorithm == Algorithm.DES) {
                DESKeySpec spec = new DESKeySpec(secret.getBytes(charset));
                SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm.getVal());
                return factory.generateSecret(spec);
            } else if (algorithm == Algorithm.DESEDE) {
                DESedeKeySpec spec = new DESedeKeySpec(secret.getBytes(charset));
                SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm.getVal());
                return factory.generateSecret(spec);
            } else if (algorithm == Algorithm.AES) {
                return new SecretKeySpec(secret.getBytes(charset), algorithm.getVal());
            }

            return null;
        }

        public static String encrypt(String data, Charset charset, Algorithm algorithm, String secret, String iv) {
            try {
                Key key = toKey(algorithm, secret, charset);
                Cipher cipher = Cipher.getInstance(algorithm.getKit());
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
                return NumerationUtil.toHex(cipher.doFinal(data.getBytes(charset)));
            } catch (Exception e) {
                throw new RuntimeException("SE encrypt error.", e);
            }
        }

        public static String decrypt(String hexData, Charset charset, Algorithm algorithm, String secret, String iv) {
            try {
                Key key = toKey(algorithm, secret, charset);
                Cipher cipher = Cipher.getInstance(algorithm.getKit());
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
                return new String(cipher.doFinal(NumerationUtil.toBytes(hexData)), charset);
            } catch (Exception e) {
                throw new RuntimeException("SE encrypt error.", e);
            }
        }

        public static String encryptByDES(String data, String secret, String iv) {
            return encrypt(data, UTF8, Algorithm.DES, secret, iv);
        }

        public static String decryptByDES(String data, String secret, String iv) {
            return decrypt(data, UTF8, Algorithm.DES, secret, iv);
        }

        public static String encryptBy3DES(String data, String secret, String iv) {
            return encrypt(data, UTF8, Algorithm.DESEDE, secret, iv);
        }

        public static String decryptBy3DES(String data, String secret, String iv) {
            return decrypt(data, UTF8, Algorithm.DESEDE, secret, iv);
        }

        public static String encryptByAES(String data, String secret, String iv) {
            return encrypt(data, UTF8, Algorithm.AES, secret, iv);
        }

        public static String decryptByAES(String data, String secret, String iv) {
            return decrypt(data, UTF8, Algorithm.AES, secret, iv);
        }
    }

    /**
     * 非对称加密（Asymmetric encryption）
     */
    private static class AE {
        private static final int KEY_SIZE_RSA = 2048;

        public static Map<String, Key> initRsaKeyPair(int keySize) {
            return KeyUtil.initKeyPair(Algorithm.RSA.getVal(), keySize);
        }

        public static byte[] rsaEncryptByPublicKey(String data, String key) {
            try {
                // 取得公钥
                KeySpec keySpec = new X509EncodedKeySpec(base64DecodeToBytes(key));
                KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getVal());
                PublicKey publicKey = keyFactory.generatePublic(keySpec);

                // 公钥加密
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(data.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("AE encryptByPublicKey error.", e);
            }
        }

        public static byte[] rsaDecryptByPrivateKey(byte[] data, byte[] bytes) {
            try {
                // 取得私钥
                KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
                KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getVal());
                PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

                // 公钥加密
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(data);
            } catch (Exception e) {
                throw new RuntimeException("AE encryptByPublicKey error.", e);
            }
        }
    }

    /**
     * 签名算法（公钥加密的逆过程）
     */
    private static class SIG {
        private static final int KEY_SIZE_RSA = 2048;

        public static Map<String, Key> initKeyPairByRSA(int keySize) {
            return KeyUtil.initKeyPair(Algorithm.RSA.getVal(), keySize);
        }

        public static byte[] signByMD5withRSA(String data, String privateKey) {
            return sign(data, UTF8, Algorithm.RSA.getVal(), Algorithm.MD5withRSA.getVal(), privateKey);
        }

        public static boolean verifyByMD5withRSA(byte[] data, byte[] publicKeyBytes, byte[] signs) {
            return verify(Algorithm.RSA.getVal(), Algorithm.MD5withRSA.getVal(), data, publicKeyBytes, signs);
        }

        /**
         * @param data          数据
         * @param cs            字符集
         * @param keyAlgorithm  密钥算法
         * @param signAlgorithm 签名算法
         * @param privateKeyStr 私钥字符串
         * @return
         */
        public static byte[] sign(String data, Charset cs, String keyAlgorithm, String signAlgorithm,
                String privateKeyStr) {
            try {
                // 取得私钥
                KeySpec keySpec = new PKCS8EncodedKeySpec(base64DecodeToBytes(privateKeyStr));
                KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
                PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

                // 签名
                Signature signature = Signature.getInstance(signAlgorithm);
                signature.initSign(privateKey);
                signature.update(data.getBytes(cs));

                return signature.sign();
            } catch (Exception e) {
                throw new RuntimeException("SIG sign error.", e);
            }
        }

        /**
         * @param keyAlgorithm   密钥算法
         * @param signAlgorithm  签名算法
         * @param data           数据
         * @param publicKeyBytes 公钥
         * @param signs          签名内容
         * @return
         */
        public static boolean verify(String keyAlgorithm, String signAlgorithm, byte[] data, byte[] publicKeyBytes,
                byte[] signs) {
            try {
                // 取得公钥
                KeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
                PublicKey publicKey = keyFactory.generatePublic(keySpec);

                // 签名验证
                Signature signature = Signature.getInstance(signAlgorithm);
                signature.initVerify(publicKey);
                signature.update(data);

                return signature.verify(signs);
            } catch (Exception e) {
                throw new RuntimeException("SIG verify error.", e);
            }
        }
    }

    private static class KeyUtil {

        private static final String PUBLIC_KEY = "PUBLIC_KEY";
        private static final String PRIVATE_KEY = "PRIVATE_KEY";

        public static Map<String, Key> initKeyPair(String algorithm, int keySize) {
            try {
                /**
                 * algorithm @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator"/>
                 */
                KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
                generator.initialize(keySize);
                KeyPair keyPair = generator.generateKeyPair();
                keyPair.getPublic();

                Map<String, Key> map = new HashMap<>();
                map.put(PUBLIC_KEY, keyPair.getPublic());
                map.put(PRIVATE_KEY, keyPair.getPrivate());

                return map;
            } catch (Exception e) {
                throw new RuntimeException("KeyUtil initKeyPair error.", e);
            }
        }

        public static String fetchPublicKey(Map<String, Key> map) {
            return base64Encode(map.get(PUBLIC_KEY).getEncoded());
        }

        public static String fetchPrivateKey(Map<String, Key> map) {
            return base64Encode(map.get(PRIVATE_KEY).getEncoded());
        }
    }

    private static class Combination {

    }

    private enum Algorithm {
        MD5("MD5", "", AbsAlgo.MD), //
        SHA("SHA-1", "", AbsAlgo.MD), //
        SHA256("SHA-256", "", AbsAlgo.MD), //
        SHA512("SHA-512", "", AbsAlgo.MD), //

        HMACMD5("HmacMD5", "", AbsAlgo.MAC), //

        DES("DES", "DES/CBC/PKCS5Padding", AbsAlgo.SE),  //
        DESEDE("DESede", "DESede/CBC/PKCS5Padding", AbsAlgo.SE),  //
        AES("AES", "AES/CBC/PKCS5Padding", AbsAlgo.SE), //

        RSA("RSA", "", AbsAlgo.AE), //
        DSA("DSA", "", AbsAlgo.AE), // 仅用于签名算法中的密钥算法

        MD5withRSA("MD5withRSA", "", AbsAlgo.SIG), //
        SHA1withRSA("SHA1withRSA", "", AbsAlgo.SIG), //
        SHA256withRSA("SHA256withRSA", "", AbsAlgo.SIG), //
        SHA512withRSA("SHA512withRSA", "", AbsAlgo.SIG), //
        SHA1withDSA("SHA1withDSA", "", AbsAlgo.SIG), //
        SHA256withDSA("SHA256withDSA", "", AbsAlgo.SIG), //
        SHA512withDSA("SHA512withDSA", "", AbsAlgo.SIG), //
        ;
        private final String val;
        private final String kit; // 密钥套件：算法/模式/填充方式
        private final AbsAlgo algo;

        Algorithm(String val, String kit, AbsAlgo algo) {
            this.val = val;
            this.kit = kit;
            this.algo = algo;
        }

        public String getVal() {
            return val;
        }

        public String getKit() {
            return kit;
        }

        public AbsAlgo getAlgo() {
            return algo;
        }
    }

    private enum AbsAlgo {

        /**
         * 摘要算法
         * <p>
         * Java8支持的算法有：[MD2, MD5, SHA-1, SHA-224, SHA-256, SHA-384, SHA-512]
         */
        MD,

        /**
         * 消息认证码
         */
        MAC,

        /**
         * 对称加密
         */
        SE,

        /**
         * 非对称加密
         */
        AE,

        /**
         * 签名算法
         * <p>
         * Java8支持的算法 @see <a href=https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Signature/>
         */
        SIG,

    }

    /**
     * 进制工具
     */
    public static class NumerationUtil {

        public static String toHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(toHex(b));
            }

            return sb.toString();
        }

        public static String toHex(byte b) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            return hex;
        }

        public static byte[] toBytes(String hexStr) {
            try {
                return Hex.decodeHex(hexStr);
            } catch (DecoderException e) {
                throw new RuntimeException("NumerationUtil toByte error.", e);
            }
        }

    }

    public static void main(String[] args) {
        // testBase64();
        // testMd();
        // testMAC();
        // testSE();
        // testAE();
        testSIG();
        // System.out.println(salt());
    }

    private static void testSIG() {
        String data = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：~";
        Map<String, Key> keyPair = SIG.initKeyPairByRSA(SIG.KEY_SIZE_RSA);
        String publicKey = KeyUtil.fetchPublicKey(keyPair);
        String privateKey = KeyUtil.fetchPrivateKey(keyPair);
        System.out.println(publicKey);
        System.out.println(publicKey.length());
        System.out.println(privateKey);
        System.out.println(privateKey.length());

        // byte[] sign = SIG.signByMD5withRSA(data, privateKey);
        // String cipher = base64Encode(sign);
        String cipher = sighByRSA(data, UTF8, Algorithm.MD5withRSA, privateKey);
        System.out.println(cipher);
        System.out.println(cipher.length());
        byte[] keys = base64DecodeToBytes(publicKey);

        // System.out.println(SIG.verifyByMD5withRSA(data.getBytes(UTF8), keys, sign));
        System.out.println(SIG.verifyByMD5withRSA(data.getBytes(UTF8), keys, base64DecodeToBytes(cipher)));
    }

    private static void testAE() {
        String data = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：~";
        Map<String, Key> keyPair = AE.initRsaKeyPair(AE.KEY_SIZE_RSA);
        String publicKey = KeyUtil.fetchPublicKey(keyPair);
        String privateKey = KeyUtil.fetchPrivateKey(keyPair);
        System.out.println(publicKey);
        System.out.println(publicKey.length());
        System.out.println(privateKey);
        System.out.println(privateKey.length());

        byte[] bytes = AE.rsaEncryptByPublicKey(data, publicKey);
        String cipher = base64Encode(bytes);
        System.out.println(cipher);
        System.out.println(cipher.length());
        byte[] keys = base64DecodeToBytes(privateKey);

        byte[] dataBytes = AE.rsaDecryptByPrivateKey(bytes, keys);
        System.out.println(new String(dataBytes, UTF8));
    }

    private static void testSE() {
        String data = "从前有座山，山里有座庙，庙里有个老和尚在跟小和尚讲着一个故事：";
        String secret = "c4680140a338cdcf";
        String iv = "4cb79d35";
        String desCipher = SE.encryptByDES(data, secret, iv);
        System.out.println(desCipher);
        System.out.println(SE.decryptByDES(desCipher, secret, iv));

        String desEdeCipher = SE.encryptBy3DES(data, secret + secret + secret, iv);
        System.out.println(desEdeCipher);
        System.out.println(SE.decryptBy3DES(desEdeCipher, secret + secret + secret, iv));

        String aesCipher = SE.encryptByAES(data, secret, "4cb79d35583f8ea6");
        System.out.println(aesCipher);
        System.out.println(SE.decryptByAES(aesCipher, secret, "4cb79d35583f8ea6"));
    }

    private static void testMAC() {

        System.out.println(MAC.init(Algorithm.HMACMD5.getVal()));
        System.out.println(MAC.encode("abc", "ttt", Algorithm.HMACMD5));
    }

    private static void testMd() {
        String str = "java";
        System.out.println(md5(str)); // java-93f725a07423fe1c889f448b33d21f46
        System.out.println(sha(str)); // java-23524be9dba14bc2f1975b37f95c3381771595c8
        System.out
                .println(sha256(str).length()); // java-38a0963a6364b09ad867aa9a66c6d009673c21e182015461da236ec361877f77
        System.out.println(
                sha512(str)); // java-4a49db19ff86bbe530c3175d6029c403f550104405c107f3caa2689f70339a55d2eadc60ed677a2394a402c66ab48b1134043cd4d1f6fa5f572134ef4a2de4fc
        System.out.println(mdBy(str, UTF8, "sha-256"));

        byte a = 2;
        System.out.println(NumerationUtil.toHex(a));
    }

    private static void testBase64() {
        String str = "java";
        System.out.println(base64Encode(str));
        System.out.println(base64Decode("amF2YQ=="));

        for (byte item : str.getBytes()) {
            System.out.println(item);
        }
    }

}
