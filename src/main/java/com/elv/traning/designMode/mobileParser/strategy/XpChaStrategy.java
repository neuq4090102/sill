package com.elv.traning.designMode.mobileParser.strategy;

import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.elv.traning.designMode.mobileParser.MobileResult;
import com.elv.traning.designMode.mobileParser.tool.MobileEnum.ServiceProviderEnum;

/**
 * 网站-新派查询网
 *
 * @author lxh
 * @since 2020-07-06
 */
public class XpChaStrategy implements IStrategy {

    private String URI = "http://shouji.xpcha.com/%s.html";

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public MobileResult fetch(int countryCode, String mobile) {
        try {
            Document doc = Jsoup.connect(String.format(URI, mobile)).timeout(3000).get();
            Elements elements = doc
                    .select("body > div[class=body_1000] > div[class=left_leirong] > dl[class=liebiao_1]"); // 直接定位到归属地信息
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
        Element element = elements.get(0);
        if (!element.text().contains("号码归属地")) {
            return null;
        }

        Elements dds = element.select("dd:lt(2)");
        if (dds.isEmpty() || dds.size() < 2) {
            return null;
        }

        String text = Optional.ofNullable(dds.get(0).ownText()).orElse("");
        String[] split = text.split("\\s");
        if (split.length < 2) {
            return null;
        }

        MobileResult mobileResult = new MobileResult();
        mobileResult.setServiceProvider(ServiceProviderEnum.fetch(dds.get(1).ownText()));
        mobileResult.setProvince(split[0]);
        mobileResult.setCity(split[1]);
        mobileResult.setFetchFrom(this.getClass().getSimpleName());

        return mobileResult;
    }
}
