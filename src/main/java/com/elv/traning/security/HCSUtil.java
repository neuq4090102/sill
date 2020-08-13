package com.elv.traning.security;

import java.nio.charset.Charset;
import java.security.Key;

import javax.crypto.spec.PBEKeySpec;

/**
 * 混合密码系统（Hybrid Crypto System）
 *
 * @author lxh
 * @since 2020-08-07
 */
public class HCSUtil {

    // SE+AE
    // PBE(page 253)

    private static Key initPBEKey(String algorithm, String password, Charset cs) {
        return KeyUtil.initSecretKey(algorithm, new PBEKeySpec(password.toCharArray()));
    }

    private static byte[] encrypt(byte[] data, String password, byte[] salt) {
        // Key key = initPBEKey()

        return null;
    }

}
