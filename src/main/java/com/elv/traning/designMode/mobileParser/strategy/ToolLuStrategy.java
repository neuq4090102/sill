package com.elv.traning.designMode.mobileParser.strategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.elv.core.util.HttpUtil;
import com.elv.core.util.JsonUtil;
import com.elv.traning.designMode.mobileParser.MobileResult;
import com.elv.traning.designMode.mobileParser.tool.MobileEnum.ServiceProviderEnum;

/**
 * 免费API-在线工具(它的网站网址是：https://tool.lu/mobile）
 *
 * @author lxh
 * @since 2020-07-03
 */
public class ToolLuStrategy implements IStrategy {

    private String URI = "https://tool.lu/mobile/ajax.html?operate=query&mobile=%s";

    private Pattern pattern = Pattern.compile(".*\"status\":(.*),\"message*");

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
        if (!matcher.find() || !"true".equals(matcher.group(1))) {
            return null;
        }

        ToolLuData toolLuData = JsonUtil.toObject(fetchResult, ToolLuData.class);
        if (toolLuData == null || toolLuData.getText() == null) {
            return null;
        }

        ToolLuText text = toolLuData.getText();
        MobileResult mobileResult = new MobileResult();
        mobileResult.setServiceProvider(ServiceProviderEnum.fetch(text.getCorp()));
        mobileResult.setProvince(text.getProvince());
        mobileResult.setCity(text.getCity());
        mobileResult.setFetchFrom(this.getClass().getSimpleName());

        return mobileResult;
    }

    /**
     * {"status":true,"message":"","text":{"mobile":1371160,"areacode":"020","province":"\u5e7f\u4e1c","city":"\u5e7f\u5dde","postcode":"510000","corp":"\u4e2d\u56fd\u79fb\u52a8","card":""}}
     */
    static class ToolLuData {
        private boolean status;
        private ToolLuText text;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public ToolLuText getText() {
            return text;
        }

        public void setText(ToolLuText text) {
            this.text = text;
        }
    }

    static class ToolLuText {
        private String mobile;
        private String areacode;
        private String province;
        private String city;
        private String postcode;
        private String corp;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAreacode() {
            return areacode;
        }

        public void setAreacode(String areacode) {
            this.areacode = areacode;
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

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getCorp() {
            return corp;
        }

        public void setCorp(String corp) {
            this.corp = corp;
        }
    }
}
