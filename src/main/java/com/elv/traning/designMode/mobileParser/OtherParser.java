package com.elv.traning.designMode.mobileParser;

/**
 * @author lxh
 * @since 2020-07-07
 */
public class OtherParser extends AbstractParser {

    private OtherParser() {
    }

    public static OtherParser of() {
        return new OtherParser();
    }

    @Override
    protected MobileResult parse(String mobile) {
        // 可自定义具体策略
        return this.defaultParse(mobile);
    }
}
