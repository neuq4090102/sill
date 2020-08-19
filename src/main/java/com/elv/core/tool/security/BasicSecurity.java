package com.elv.core.tool.security;

import java.nio.charset.Charset;
import java.util.Optional;

import com.elv.core.constant.SecurityEnum;

/**
 * 密钥工具基础
 *
 * @author lxh
 * @since 2020-08-07
 */
public abstract class BasicSecurity {

    protected Charset cs = SecurityEnum.UTF8; // 字符集

    public void init() {
        cs = Optional.ofNullable(cs).orElse(SecurityEnum.UTF8);
    }

}
