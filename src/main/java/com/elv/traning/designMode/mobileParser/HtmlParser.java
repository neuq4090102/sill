package com.elv.traning.designMode.mobileParser;

/**
 * 网页抓取策略
 *
 * @author lxh
 * @since 2020-06-29
 */
public class HtmlParser extends AbstractParser {

    private HtmlParser() {
    }

    public static HtmlParser of() {
        return new HtmlParser();
    }

    @Override
    protected MobileResult parse(String mobile) {
        // 可自定义具体策略
        return this.defaultParse(mobile);
    }

}
