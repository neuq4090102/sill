package com.elv.traning.designMode.mobileParser.strategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.elv.traning.designMode.mobileParser.MobileResult;
import com.elv.traning.designMode.mobileParser.tool.MobileEnum.ServiceProviderEnum;

/**
 * 网站-归属地
 *
 * @author lxh
 * @since 2020-06-30
 */
public class GuisdStrategy implements IStrategy {

    private String URI = "http://www.guisd.com/n/%s.html";

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public MobileResult fetch(int countryCode, String mobile) {
        try {
            Document doc = Jsoup.connect(String.format(URI, mobile)).timeout(3000).get();
            Elements elements = doc
                    .select("body > div[class=wrap] > div[class=mleft] > div[class=result] > dl > #c1 "); // 直接定位到归属地信息
            return this.confirm(elements);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private MobileResult confirm(Elements elements) {
        if (elements.isEmpty()) {
            return null;
        }

        Elements links = elements.select("a[title]");
        if (links.isEmpty() || links.size() < 2) {
            return null;
        }

        String city = links.get(1).text();
        if ("未知".equals(city)) { // 城市未知的情况下，有可能是卡号未开，该网站识别不了
            return null;
        }

        String serviceProvider = "";
        Matcher matcher = Pattern.compile("运营商：(.*) 省份：").matcher(elements.text());
        if (matcher.find()) {
            serviceProvider = ServiceProviderEnum.fetch(matcher.group(1));
        }

        MobileResult mobileResult = new MobileResult();
        mobileResult.setServiceProvider(serviceProvider);
        mobileResult.setProvince(links.get(0).text());
        mobileResult.setCity(city);
        mobileResult.setFetchFrom(this.getClass().getSimpleName());

        return mobileResult;
    }
}
