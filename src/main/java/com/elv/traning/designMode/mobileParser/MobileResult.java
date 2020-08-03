package com.elv.traning.designMode.mobileParser;

import com.elv.core.util.StrUtil;

/**
 * @author lxh
 * @since 2020-06-29
 */
public class MobileResult {

    private String serviceProvider; // 运营商
    private String country = "中国"; // 国家
    private String province; // 省份
    private String city; // 城市
    private String fetchFrom; // 获取途径

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getFetchFrom() {
        return fetchFrom;
    }

    public void setFetchFrom(String fetchFrom) {
        this.fetchFrom = fetchFrom;
    }

    public boolean isComplete() {
        if (StrUtil.isBlank(getProvince())) {
            return false;
        } else if (StrUtil.isBlank(getCity())) {
            return false;
        }
        return true;
    }

}
