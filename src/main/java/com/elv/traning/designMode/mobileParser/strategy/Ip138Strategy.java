package com.elv.traning.designMode.mobileParser.strategy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.elv.traning.designMode.mobileParser.MobileResult;
import com.elv.traning.designMode.mobileParser.tool.MobileEnum.ServiceProviderEnum;

/**
 * 网站-iP138
 *
 * @author lxh
 * @since 2020-06-30
 */
public class Ip138Strategy implements IStrategy {

    private String URI = "https://www.ip138.com/mobile.asp?mobile=%s&action=mobile";

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public MobileResult fetch(int countryCode, String mobile) {
        try {
            Document doc = Jsoup.connect(String.format(URI, mobile)).timeout(7000).get();
            Elements elements = doc
                    .select("body > div[class=wrapper] > div[class=container] > div[class=content] > div[class=mod-panel] > div[class=bd] > div[class=table] > table > tbody"); // 直接定位到归属地信息
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

        Elements trs = elements.select("tr");
        if (trs.isEmpty() || trs.size() < 3) {
            return null;
        }

        String province = "";
        String city = "";
        Elements locationSpans = trs.get(1).select("span");
        if (!locationSpans.isEmpty() && locationSpans.size() == 1) {
            String text = locationSpans.get(0).text();
            String[] split = text.split("\\s");
            if (split.length == 2) {
                province = split[0];
                city = split[1];
            }
        }

        String serviceProvider = "";
        Elements spSpans = trs.get(2).select("span");
        if (!spSpans.isEmpty() && spSpans.size() == 1) {
            serviceProvider = ServiceProviderEnum.fetch(spSpans.text());
        }

        MobileResult mobileResult = new MobileResult();
        mobileResult.setServiceProvider(serviceProvider);
        mobileResult.setProvince(province);
        mobileResult.setCity(city);
        mobileResult.setFetchFrom(this.getClass().getSimpleName());

        return mobileResult;

    }
}
