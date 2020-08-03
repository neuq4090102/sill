package com.elv.traning.designMode.mobileParser.strategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.elv.core.util.HttpUtil;
import com.elv.core.util.JsonUtil;
import com.elv.traning.designMode.mobileParser.MobileResult;
import com.elv.traning.designMode.mobileParser.tool.MobileEnum.ServiceProviderEnum;

/**
 * 免费API-百付宝
 * <p>
 * 注意：该接口只能确定服务提供商及省份信息，无法确定城市信息
 *
 * @author lxh
 * @since 2020-06-29
 */
public class BaiFuBaoStrategy implements IStrategy {

    private String URI = "https://www.baifubao.com/callback?cmd=1059&callback=phone&phone=%s";

    private Pattern pattern = Pattern.compile("phone\\(.*?\"meta\":(.*),\"data\":(.*)\\}\\)");

    @Override
    public boolean enable() {
        return false;
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
        BfbMeta bfbMeta = JsonUtil.toObject(matcher.group(1), BfbMeta.class);
        if (bfbMeta == null || !"0".equals(bfbMeta.getResult())) { // 未成功获取
            return null;
        }
        BfbData bfbData = JsonUtil.toObject(matcher.group(2), BfbData.class);
        if (bfbData == null) {
            return null;
        }

        MobileResult mobileResult = new MobileResult();
        mobileResult.setServiceProvider(ServiceProviderEnum.fetch(bfbData.getOperator()));
        mobileResult.setProvince(bfbData.getArea());
        mobileResult.setFetchFrom(this.getClass().getSimpleName());

        return mobileResult;
    }

    /**
     * {"result":"0","result_info":"","jump_url":""}
     */
    static class BfbMeta {

        private String result;
        private String result_info;
        private String jump_url;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getResult_info() {
            return result_info;
        }

        public void setResult_info(String result_info) {
            this.result_info = result_info;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }
    }

    /**
     * {"operator":"移动","area":"河北","area_operator":"河北移动","support_price":{"1000":"999","10000":"9990","2000":"1998","20000":"19980","3000":"2997","30000":"29970","5000":"4995","50000":"49950"},"promotion_info":null}
     */
    static class BfbData {

        private String operator;
        private String area;
        private String area_operator;

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getArea_operator() {
            return area_operator;
        }

        public void setArea_operator(String area_operator) {
            this.area_operator = area_operator;
        }
    }

}
