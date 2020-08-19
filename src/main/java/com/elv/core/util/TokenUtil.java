package com.elv.core.util;

import com.elv.core.tool.db.jdbc.BasicQuery;
import com.elv.core.tool.db.model.SecurityConfig;
import com.elv.core.tool.unique.token.AbstractTokenUtil;

/**
 * 自定义token工具
 *
 * @author lxh
 * @since 2020-08-10
 */
public class TokenUtil extends AbstractTokenUtil {

    @Override
    public String getSecretKey() {
        return querySecretConfig().getSecretKey();
    }

    @Override
    public String getIV() {
        return querySecretConfig().getIv();
    }

    @Override
    public String generateToken(String plaintext) {
        return defaultGenerateToken(plaintext);
    }

    @Override
    public String parseToken(String token) {
        return defaultParseToken(token);
    }

    private SecurityConfig querySecretConfig() {
        return BasicQuery.secretConfig();
    }

    public static void main(String[] args) {

        // "salt|appId|timestamp|other";
        StringBuilder sb = new StringBuilder();
        sb.append(getSalt());
        sb.append("|");
        sb.append("1");
        sb.append("|");
        sb.append(Dater.now().getTimestamp());
        sb.append("|");
        sb.append("other");

        TokenUtil tokenUtil = new TokenUtil();
        String plaintext = sb.toString();
        System.out.println(plaintext);
        System.out.println(tokenUtil.generateToken(plaintext));
        System.out.println(tokenUtil.parseToken(
                "3f60f6e5321ba0bf50ca5a316edc1e0da17b3d9362a6a917fc61ed2886faea375b00a4129e5788fdbcd33b3c9bfaa06ebf84f7b6ded2473f8362adf84131aa6a6a2449cae2f73032955e18cf0957335c"));

    }

    private static String getSalt() {
        return SecurityUtil.salt();
    }
}
