package com.elv.traning.designMode.mobileParser;

import java.util.ArrayList;
import java.util.List;

import com.elv.core.util.JsonUtil;
import com.elv.traning.designMode.mobileParser.strategy.GoogleStrategy;
import com.elv.traning.designMode.mobileParser.strategy.GuisdStrategy;
import com.elv.traning.designMode.mobileParser.strategy.IStrategy;
import com.elv.traning.designMode.mobileParser.strategy.Ip138Strategy;
import com.elv.traning.designMode.mobileParser.strategy.ShouJiStrategy;
import com.elv.traning.designMode.mobileParser.strategy.ToolLuStrategy;
import com.elv.traning.designMode.mobileParser.strategy.XpChaStrategy;

/**
 * @author lxh
 * @since 2020-06-29
 */
public class ParseMobile {

    /**
     * 内部测试使用
     *
     * @return com.elv.traning.designMode.mobileParser.AbstractParser
     */
    public static AbstractParser test() {

        List<IStrategy> strategies = new ArrayList<>();
        // strategies.add(new TianTianXieYeStrategy());
        strategies.add(new ToolLuStrategy());

        return OtherParser.of().strategies(strategies);
    }

    public static AbstractParser assemble() {
        // 本地工具&数据库
        List<IStrategy> localStrategies = new ArrayList<>();
        localStrategies.add(new GoogleStrategy());

        // 免费API
        List<IStrategy> freeApiStrategies = new ArrayList<>();
        freeApiStrategies.add(new ShouJiStrategy());
        freeApiStrategies.add(new ToolLuStrategy());
        // freeApiStrategies.add(new TaoBaoStrategy());
        // freeApiStrategies.add(new BaiFuBaoStrategy());

        // 网页分析
        List<IStrategy> htmlStrategies = new ArrayList<>();
        htmlStrategies.add(new GuisdStrategy());
        htmlStrategies.add(new XpChaStrategy());
        htmlStrategies.add(new Ip138Strategy());

        // 收费API
        List<IStrategy> chargeableStrategies = new ArrayList<>();

        AbstractParser localParser = LocalParser.of().strategies(localStrategies);
        AbstractParser freeApiParser = FreeApiParser.of().strategies(freeApiStrategies);
        AbstractParser htmlParser = HtmlParser.of().strategies(htmlStrategies);
        AbstractParser chargeableApiParser = ChargeableApiParser.of().strategies(chargeableStrategies);

        // 确定抓取优先级:Local > FreeApi > Html > Chargeable
        htmlParser.setNextParser(chargeableApiParser);
        freeApiParser.setNextParser(htmlParser);
        localParser.setNextParser(freeApiParser);

        return localParser;
    }

    public static void main(String[] args) {
        // String mobile = "15232321504"; // 移动-河北秦皇岛
        // String mobile = "13711600002"; // 移动-广东广州
        // String mobile = "18584838281"; // 联通-四川成都
        // String mobile = "18128577773"; // 电信-广州东莞
        String mobile = "0086-17614769319"; // 联通-内蒙赤峰
        // String mobile = "7-4956992956"; // 俄罗斯

        // AbstractParser crawlMobile = assemble();
        AbstractParser crawlMobile = test();
        MobileResult mobileResult = crawlMobile.deal(mobile);

        System.out.println(JsonUtil.toJson(mobileResult));

        if (mobileResult != null && mobileResult.isComplete()) {
            System.out.println("success to fetch.");
        }
    }
}
