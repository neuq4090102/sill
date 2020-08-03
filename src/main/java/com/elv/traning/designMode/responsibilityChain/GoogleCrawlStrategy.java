package com.elv.traning.designMode.responsibilityChain;

/**
 * 抽象抓取策略
 * @author lxh
 * @since 2020-05-20
 */
public class GoogleCrawlStrategy extends CrawlStrategy {

    @Override
    protected StrategyStore fetch() {
         return null;
//        return new StrategyStore("Guisd");
    }
}
