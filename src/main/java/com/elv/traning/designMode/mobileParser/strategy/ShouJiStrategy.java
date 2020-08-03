package com.elv.traning.designMode.mobileParser.strategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.elv.core.util.Dater;
import com.elv.core.util.HttpUtil;
import com.elv.core.util.JsonUtil;
import com.elv.traning.designMode.mobileParser.MobileResult;
import com.elv.traning.designMode.mobileParser.tool.MobileEnum.ServiceProviderEnum;

/**
 * 网站-手机在线(在JS文件找到的URI)
 *
 * @author lxh
 * @since 2020-07-06
 */
public class ShouJiStrategy implements IStrategy {

    private String URI =
            "https://v.showji.com/Locating/showji.com20180331.aspx?m=%s&output=json&callback=querycallback&timestamp="
                    + Dater.now().getDate().getTime();

    private Pattern pattern = Pattern.compile("querycallback\\((.*)\\);");

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public MobileResult fetch(int countryCode, String mobile) {
        String fetchResult = HttpUtil.get(String.format(URI, mobile));
        return this.confirm(fetchResult);
    }

    private MobileResult confirm(String fetchResult) {
        if (fetchResult == null) {
            return null;
        }

        Matcher matcher = pattern.matcher(fetchResult);
        if (!matcher.find()) {
            return null;
        }

        ShouJiData shouJiData = JsonUtil.toObject(matcher.group(1).toLowerCase(), ShouJiData.class); // 注意：结果处理成了小写
        if (shouJiData == null || !"true".equals(shouJiData.getQueryresult())) {
            return null;
        }
        MobileResult mobileResult = new MobileResult();
        mobileResult.setServiceProvider(ServiceProviderEnum.fetch(shouJiData.getCorp()));
        mobileResult.setProvince(shouJiData.getProvince());
        mobileResult.setCity(shouJiData.getCity());
        mobileResult.setFetchFrom(this.getClass().getSimpleName());

        return mobileResult;
    }

    /**
     * querycallback({"Mobile":"18584838281","QueryResult":"True","TO":"中国联通","Corp":"中国联通","Province":"四川","City":"成都","AreaCode":"028","PostCode":"610000","VNO":"","Card":""});
     */
    static class ShouJiData {
        private String mobile;
        private String queryresult;
        private String to;
        private String corp;
        private String province;
        private String city;
        private String areacode;
        private String postcode;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getQueryresult() {
            return queryresult;
        }

        public void setQueryresult(String queryresult) {
            this.queryresult = queryresult;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getCorp() {
            return corp;
        }

        public void setCorp(String corp) {
            this.corp = corp;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAreacode() {
            return areacode;
        }

        public void setAreacode(String areacode) {
            this.areacode = areacode;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }
    }

}
