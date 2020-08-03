package com.elv.traning.designMode.mobileParser.strategy;

import com.elv.core.util.HttpUtil;
import com.elv.traning.designMode.mobileParser.MobileResult;
import com.elv.traning.designMode.mobileParser.tool.MobileEnum.ServiceProviderEnum;

/**
 * 免费API-淘宝
 * <p>
 * 注意：该接口只能确定服务提供商及省份信息，无法确定城市信息
 *
 * @author lxh
 * @since 2020-06-29
 */
public class TaoBaoStrategy implements IStrategy {

    private String URI = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=%s";

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public MobileResult fetch(int countryCode, String mobile) {
        String fetchResult = HttpUtil.get(String.format(URI, mobile));
        return this.confirm(fetchResult);
    }

    /**
     * __GetZoneResult_ = {
     * mts:'18584838281',
     * province:'四川',
     * catName:'中国移动',
     * telString:'18584838281',
     * areaVid:'30508',
     * ispVid:'3236139',
     * carrier:'四川移动'
     * }
     *
     * @param tbData
     * @return
     */
    private MobileResult confirm(String fetchResult) {
        if (fetchResult == null || !fetchResult.startsWith("__GetZoneResult_ =")) {
            return null;
        }

        String serviceProvider = ""; // 运营商
        String province = ""; // 省份
        for (String field : fetchResult.substring(19).split(",")) {
            String[] split = field.split(":");
            if (split != null && split.length == 2) {
                String fieldName = split[0].trim();
                String fieldValue = split[1].replaceAll("'", "");
                if ("catName".equals(fieldName)) {
                    serviceProvider = ServiceProviderEnum.fetch(fieldValue);
                } else if ("province".equals(fieldName)) {
                    province = fieldValue;
                }
            }
        }

        MobileResult mobileResult = new MobileResult();
        mobileResult.setServiceProvider(serviceProvider);
        mobileResult.setProvince(province);
        mobileResult.setFetchFrom(this.getClass().getSimpleName());

        return mobileResult;
    }

}


