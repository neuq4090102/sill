package com.elv.core.util;

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

/**
 * @author lxh
 * @since 2020-06-16
 */
public class SecurityUtil {

    private static final Charset UTF8;
    private static final SecureRandom RANDOM;

    static {
        UTF8 = StandardCharsets.UTF_8;
        RANDOM = new SecureRandom();
    }

    private SecurityUtil() {
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

    public static String md5(String str, Charset charset) {
        return mdToHex(Algorithm.MD5.getVal(), str, charset);
    }

    public static String sha(String str) {
        return mdToHex(Algorithm.SHA.getVal(), str);
    }

    public static String sha(String str, Charset charset) {
        return mdToHex(Algorithm.SHA.getVal(), str, charset);
    }

    public static String sha256(String str) {
        return mdToHex(Algorithm.SHA256.getVal(), str);
    }

    public static String sha512(String str) {
        return mdToHex(Algorithm.SHA512.getVal(), str);
    }

    public static String mdBy(String str, Charset charset, String algorithm) {
        return mdToHex(algorithm, str, charset);
    }

    public static String toHex(byte[] bytes) {
        return NumerationUtil.toHex(bytes);
    }

    public static String hexToStr(String hexStr, Charset charset) {
        return new String(NumerationUtil.toBytes(hexStr), charset);
    }

    public static String salt() {
        return NumerationUtil.toHex(RANDOM.generateSeed(8));
    }

    // ******************************** base64 ********************************* //

    /**
     * base64
     */
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

    // ******************************** MD ********************************* //

    /**
     * 信息摘要
     */
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

    // ******************************** MAC ********************************* //

    /**
     * 消息认证码
     */
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

    // ******************************** SE ********************************* //

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

    // ******************************** SE ********************************* //

    /**
     *
     */
    private static class AE {

        private static final String ALGORITHM = "RSA";
        private static final String PUBLIC_KEY = "PUBLIC_KEY";
        private static final String PRIVATE_KEY = "PRIVATE_KEY";
        private static final int KEY_SIZE = 2048;

        public static Map<String, Key> initKeyPair() {
            try {
                KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
                generator.initialize(KEY_SIZE);
                KeyPair keyPair = generator.generateKeyPair();
                keyPair.getPublic();

                Map<String, Key> map = new HashMap<>();
                map.put(PUBLIC_KEY, keyPair.getPublic());
                map.put(PRIVATE_KEY, keyPair.getPrivate());
                return map;
            } catch (Exception e) {
                throw new RuntimeException("AE initKeyPair error.", e);
            }
        }

        public static String fetchPublicKey(Map<String, Key> map) {
            return base64Encode(map.get(PUBLIC_KEY).getEncoded());
        }

        public static String fetchPrivateKey(Map<String, Key> map) {
            return base64Encode(map.get(PRIVATE_KEY).getEncoded());
        }

        public static byte[] encryptByPublicKey(String data, String key) {
            try {
                // 取得公钥
                KeySpec keySpec = new X509EncodedKeySpec(base64DecodeToBytes(key));
                KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
                PublicKey publicKey = keyFactory.generatePublic(keySpec);

                // 公钥加密
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(data.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("AE encryptByPublicKey error.", e);
            }
        }

        public static byte[] decryptByPrivateKey(byte[] datas, byte[] bytes) {
            try {
                // 取得私钥
                KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
                KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
                PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

                // 公钥加密
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(datas);
            } catch (Exception e) {
                throw new RuntimeException("AE encryptByPublicKey error.", e);
            }
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

}
