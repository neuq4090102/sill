package com.elv.traning.designMode.responsibilityChain;

/**
 * @author lxh
 * @date 2020-05-20
 */
public class CrawlStrategyDemo {

    public static void main(String[] args) {
        CrawlStrategy crawlStrategy = CrawlStrategyUtil.assemble();
        crawlStrategy.deal();
    }
}
