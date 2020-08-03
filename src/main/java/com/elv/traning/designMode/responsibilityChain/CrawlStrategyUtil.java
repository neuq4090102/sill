package com.elv.traning.designMode.responsibilityChain;

/**
 * @author lxh
 * @since 2020-05-20
 */
public class CrawlStrategyUtil {

    public static CrawlStrategy assemble() {

        CrawlStrategy guisd = new GuisdCrawlStrategy();
        CrawlStrategy baidu = new BaiDuCrawlStrategy();
        CrawlStrategy google = new GoogleCrawlStrategy();

        guisd.setNextCrawlStrategy(baidu);
        baidu.setNextCrawlStrategy(google);

        return guisd;
    }
}
