package com.elv.core.tool.security.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.elv.core.constant.SecurityEnum.AbsAlgo;
import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.util.Assert;
import com.elv.core.tool.security.AbstractMDUtil;
import com.elv.core.tool.security.NumerationUtil;

/**
 * 消息摘要
 *
 * @author lxh
 * @since 2020-08-18
 */
public class MDUtil extends AbstractMDUtil {

    private String algorithm; // 摘要算法

    private MDUtil() {
    }

    public static MDUtil of() {
        return new MDUtil();
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    public MDUtil algorithm(Algorithm algorithm) {
        Assert.isTrue(!algorithm.belong(AbsAlgo.MD), "MDUtil#algorithm invalid algorithm:" + algorithm.getVal());
        this.algorithm = algorithm.getVal();
        return this;
    }

    public MDUtil algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public MDUtil cs(Charset cs) {
        this.cs = cs;
        return this;
    }

    /**
     * 消息摘要
     *
     * @param msg 消息
     * @return java.lang.String
     */
    public String md(String msg) {
        Assert.notBlank("MDUtil#md param not blank.", msg, algorithm);
        init();
        return NumerationUtil.toHex(md(msg.getBytes(cs)));
    }

    public static void main(String[] args) {
        testMd();
    }

    private static void testMd() {
        String msg = "java";

        System.out.println(MDUtil.of().algorithm(Algorithm.MD5).cs(StandardCharsets.UTF_16).md(msg));

        System.out.println(MDUtil.of().algorithm(Algorithm.SHA).md(msg));

        String md256 = MDUtil.of().algorithm(Algorithm.SHA256).md(msg);
        System.out.println(md256);
        System.out.println("sha-256的长度：" + md256.length());

        String md512 = MDUtil.of().algorithm(Algorithm.SHA512).md(msg);
        System.out.println(md512);
        System.out.println("sh512的长度：" + md512.length());
    }

}
