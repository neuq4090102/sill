package com.elv.traning.designMode.responsibilityChain;

/**
 * 抽象抓取策略
 *
 * @author lxh
 * @date 2020-05-20
 */
public abstract class CrawlStrategy {

    protected CrawlStrategy nextCrawlStrategy;

    abstract protected StrategyStore fetch();

    public void deal() {
        StrategyStore strategyStore = this.fetch();
        if (strategyStore != null) {
            System.out
                    .println(this.getClass().getSimpleName() + " do it, and fetch content:" + strategyStore.getName());
            return;
        } else if (this.getNextCrawlStrategy() != null) {
            System.out.println(this.getClass().getSimpleName() + " can't do it, but has other strategy.");
            this.getNextCrawlStrategy().deal();
        } else {
            System.out.println(this.getClass().getSimpleName() + " can't do it, all can't deal.");
        }
    }

    public CrawlStrategy getNextCrawlStrategy() {
        return nextCrawlStrategy;
    }

    public void setNextCrawlStrategy(CrawlStrategy nextCrawlStrategy) {
        this.nextCrawlStrategy = nextCrawlStrategy;
    }
}
