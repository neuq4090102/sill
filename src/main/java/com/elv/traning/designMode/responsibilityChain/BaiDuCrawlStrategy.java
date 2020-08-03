package com.elv.traning.designMode.responsibilityChain;

/**
 * @author lxh
 * @since 2020-05-20
 */
public class BaiDuCrawlStrategy extends CrawlStrategy {

    @Override
    protected StrategyStore fetch() {
        // return null;
        return new StrategyStore("Guisd");
    }
}
