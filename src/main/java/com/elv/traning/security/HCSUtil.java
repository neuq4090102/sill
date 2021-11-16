package com.elv.traning.security;

import java.nio.charset.Charset;

import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.tool.security.impl.DSUtil;
import com.elv.core.tool.security.impl.MDUtil;
import com.elv.core.util.Dater;

/**
 * 混合密码系统（Hybrid Crypto System）
 * <p>
 * 1.SE+AE，对称加密明文后，密文作为非对称加密的明文再加密
 * 2.md+sign，使用数字签名时，对消息先进行消息摘要，结果作为签名对消息
 *
 * @author lxh
 * @since 2020-08-07
 */
public class HCSUtil {

    /**
     * 数字签名
     * <p>
     * 消息先做消息摘要，再进行加密
     *
     * @param msg           消息
     * @param mdAlgorithm   摘要算法
     * @param signAlgorithm 签名算法
     * @param privateKey    私钥
     * @param cs            字符集
     * @return java.lang.String
     */
    public static String signByRSAWithMD(String msg, Algorithm mdAlgorithm, Algorithm signAlgorithm, String privateKey,
            Charset cs) {
        long t1 = Dater.now().ts();
        String md = MDUtil.of().algorithm(mdAlgorithm).cs(cs).md(msg);
        System.out.println("time1:" + (Dater.now().ts() - t1));
        return DSUtil.of().signAlgorithm(signAlgorithm).keyAlgorithm(Algorithm.RSA).privateKey(privateKey).cs(cs)
                .sign(md);
    }

    public static void main(String[] args) {
        testSign();
    }

    private static void testSign() {
        String msg = "java";

        Algorithm mdAlgorithm = Algorithm.SHA256;
        Algorithm keyAlgorithm = Algorithm.RSA;
        Algorithm signAlgorithm = Algorithm.SHA256withRSA;

        // Map<String, Key> keyPair = KeyUtil.initKeyPair(keyAlgorithm.getVal(), 2048);
        // byte[] publicKeys = KeyUtil.fetchPublicKey(keyPair);
        // byte[] privateKeys = KeyUtil.fetchPrivateKey(keyPair);
        //
        // String publicKey = Base64Util.encode(publicKeys);
        // String privateKey = Base64Util.encode(privateKeys);
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXHVKwuAh5T3gs/Duc9a/m+1P0+h0DWuH6H6ZsQFzcQ3h0j5M22qzG18DWC6ksPxlYyFQI1zf18l4fpqzhoVWsPHuC7/D/1CJycY+8ZpSZNW0cU3Fle6iXPcZgypOzN24FEAxOYrdBfP/00wt/NXgBA06CekJdEWyFMiPMovWFKvJyFKIhbnX+77jM1qo3hi4VqlxePtiAzGVxUK0jHguMEZlaOTf/9wu/qNmCaNdofPprklXcufJnXeJhtwMAeiSe014uknKzEixgm3QmsXqWul5Vx3Ffcb4eSEexXnRwPqG3lcOv0oWGXmHDp2Vbka/p5UexxZ4yVkEtrS8P4PsPAgMBAAECggEAURQXmN+1FDmSVjijV4zu6zSwEEHeLT2m/4fZyD0kVb4RtCCQsR7VEv8R5QZup2HXnK4ngHLRALIg1ESef5ULnZT6A//vQ6QzM0pA/6neUvG6cDdJIVUe2L75YFdwtfionCjHIYuPnaCOyHIvufobQBfhGNq2Hq9JlZGYaaSJrP/R628FVGlj6iEKvoHijIItp98lxse8IY8O/5SXzPPvP8iqDAMu1vnEYjvTVs8Fn3M99BEQn4Aau/IansCLvg+CTO1P0vxZCo5ZeWbLi3+Vgp41f080+Laafl28xCgziUQ8DPCtpYE6ktdv9kVkbyech2he3D225kDaJjFyRhwzqQKBgQDIJdm9L5uRlK7cQfGFKH4gPzDF2/UhaGehlvJ6cIXQIuyjHV5XgmI7uxwupkQTsz7GsCVZ+fWGz+j3t594hu8c/eeWTUjIJcE6p04w2GaKSLljJIcTabZPaULRUfHBO8+kSaF4NPO/Jnko9b6n9ONI/p8dAcwoaWd/XxN33XorjQKBgQDBSKJMnTS7HBOzxLf0fQo8qjJ2D5E5R+lJJvBOIidifI/RNklSRGWTKTvZ3V8R5VSz6mBzCif2lLwqZd3GWB31tVpDKe/6oS520XpdgXrqlLLIJVfHWA6JVsZvZ/yQA+0Ru7QT3v+JOeYl3sSWbrnKdM0bTKxUqqt/e4FQzxCMCwKBgQCfub0rgqvHIX3unt2s46TL/UHiFOhqcdSKG1mFJvlTIhvxDIX2lkHo4yjZfazEf/5wN7CHPriXjqKf1jV4zU5Tt7kytJiJyb+MHGqFBp3Ga8Uu/r6hWnK4qws82wVwrVfHB0/KC5AeEwkMDBnMFd2wqiq2znFsKroC5xA1T1WpHQKBgA5VUJPV5J8ridp+8BVVYHj3Nh7DMMXKq2YdWRyyM7F+AzxEhlNvdwjeue2CGRa+pvVgoB5n1mbWyM1sGHIo/q/F7xej+IPhiAyPpD9i5raWqFtnSYci5JESukxwra4QXHhALy56DwEiJgsQ5gsx0t1MpBkuKmls7qLZidFwSY3ZAoGAAevTx62BoAkY06kKyrfT7TKBhMQE8/gbRMX/4vBmGWoBsVZAV/iYaXFDJHDGzAz3xh3A702Dx3dREKJ9kt9VgTwOvneaJaESLXBFsvEFmRqAmwcvPhXH3gGjOytlm2McxbnK11I2qdTXmPiva7DEevoJl4afut+c4KaxUFFebGk=";
        // System.out.println(publicKey);
        // System.out.println(publicKey.length());
        // System.out.println(privateKey);
        // System.out.println(privateKey.length());

        long t1 = Dater.now().ts();
        String signByRSAWithMD = signByRSAWithMD(msg, mdAlgorithm, signAlgorithm, privateKey, null);
        System.out.println("time:" + (Dater.now().ts() - t1));
        System.out.println(signByRSAWithMD);
        System.out.println(signByRSAWithMD.length());
    }

}
