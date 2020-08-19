package com.elv.core.tool.unique.token;

import com.elv.core.util.Assert;
import com.elv.core.util.StrUtil;
import com.elv.core.util.SecurityUtil;

/**
 * token 抽象工具
 *
 * @author lxh
 * @since 2020-08-10
 */
public abstract class AbstractTokenUtil {

    /**
     * 对称加密密钥
     *
     * @return java.lang.String
     */
    public abstract String getSecretKey();

    /**
     * 初始化向量
     * <p>
     * 跟对称加密的工作模式有关
     *
     * @return java.lang.String
     */
    public abstract String getIV();

    /**
     * 生成token
     *
     * @param plaintext 明文
     * @return java.lang.String
     */
    public abstract String generateToken(String plaintext);

    /**
     * 解析token
     *
     * @param token token
     * @return java.lang.String
     */
    public abstract String parseToken(String token);

    /**
     * 默认token生成
     *
     * @param plaintext 明文
     * @return java.lang.String
     */
    public String defaultGenerateToken(String plaintext) {
        String secretKey = this.getSecretKey();
        String iv = this.getIV();
        Assert.isTrue(StrUtil.isBlank(secretKey), "AbstractTokenUtil#getSecretKey is blank.");
        Assert.isTrue(secretKey.length() != 16, "AbstractTokenUtil#getSecretKey's length should be 16.");
        Assert.isTrue(StrUtil.isBlank(iv), "AbstractTokenUtil#getIV is blank.");
        Assert.isTrue(iv.length() != 16, "AbstractTokenUtil#getIV's length should be 16.");

        String hexCipherText = SecurityUtil.encryptByAES(plaintext, secretKey, iv);
        return SecurityUtil.sha256(hexCipherText) + hexCipherText;
    }

    /**
     * 默认token解析
     *
     * @param token token
     * @return java.lang.String
     */
    public String defaultParseToken(String token) {
        int tokenLimitLength = 64; // sha256后，16进制的长度
        Assert.isTrue(StrUtil.isBlank(token), "token is blank.");
        Assert.isTrue(token.length() <= tokenLimitLength, "token is invalid.");

        String subToken = token.substring(tokenLimitLength);
        return SecurityUtil.decryptByAES(subToken, getSecretKey(), getIV());
    }
}
