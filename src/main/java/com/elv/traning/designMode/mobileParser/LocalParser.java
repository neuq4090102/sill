package com.elv.traning.designMode.mobileParser;

/**
 * 本地解析（可以利用工具jar或者是本地DB）
 *
 * @author lxh
 * @since 2020-07-07
 */
public class LocalParser extends AbstractParser {

    private LocalParser() {
    }

    public static LocalParser of() {
        return new LocalParser();
    }

    @Override
    protected MobileResult parse(String mobile) {
        // 可自定义具体策略
        return this.defaultParse(mobile);
    }
}
