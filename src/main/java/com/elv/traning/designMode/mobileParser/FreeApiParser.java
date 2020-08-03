package com.elv.traning.designMode.mobileParser;

/**
 * 免费API策略
 *
 * @author lxh
 * @since 2020-06-29
 */
public class FreeApiParser extends AbstractParser {

    private FreeApiParser() {
    }

    public static FreeApiParser of() {
        return new FreeApiParser();
    }

    @Override
    protected MobileResult parse(String mobile) {
        // 可自定义具体策略
        return this.defaultParse(mobile);
    }
}
