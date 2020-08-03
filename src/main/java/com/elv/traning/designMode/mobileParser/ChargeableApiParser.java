package com.elv.traning.designMode.mobileParser;

/**
 * 收费API策略
 *
 * @author lxh
 * @since 2020-06-29
 */
public class ChargeableApiParser extends AbstractParser {

    private ChargeableApiParser() {
    }

    public static ChargeableApiParser of() {
        return new ChargeableApiParser();
    }

    @Override
    protected MobileResult parse(String mobile) {
        // 可自定义具体策略
        return this.defaultParse(mobile);
    }
}
