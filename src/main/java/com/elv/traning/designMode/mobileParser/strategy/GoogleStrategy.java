package com.elv.traning.designMode.mobileParser.strategy;

import com.elv.core.constant.Const;
import com.elv.core.util.PhoneUtil;
import com.elv.core.util.StrUtil;
import com.elv.traning.designMode.mobileParser.MobileResult;
import com.elv.traning.designMode.mobileParser.tool.MobileEnum.ServiceProviderEnum;

/**
 * @author lxh
 * @since 2020-07-07
 */
public class GoogleStrategy implements IStrategy {

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public MobileResult fetch(int countryCode, String mobile) {
        return this.confirm(countryCode, mobile);
    }

    private MobileResult confirm(int countryCode, String mobile) {
        String geo = PhoneUtil.getGeo(countryCode, mobile);
        if (StrUtil.isBlank(geo)) {
            return null;
        }

        String country = "";
        String provice = "";
        String city = "";
        if (countryCode == Const.COUNTRY_CODE_CHINA) {
            for (String municipality : PhoneUtil.getMunicipalities()) {
                if (geo.startsWith(municipality)) {
                    provice = municipality;
                    city = municipality;
                    break;
                }
            }

            for (String autonomousRegion : PhoneUtil.getAutonomousRegions()) {
                if (geo.startsWith(autonomousRegion)) {
                    provice = autonomousRegion;
                    city = geo.replace(provice, "");
                    break;
                }
            }

            String[] geoArr = geo.split("省");
            if (geoArr.length == 2) {
                provice = geoArr[0];
                city = geoArr[1].replace("市", "");
            }

            if (provice == "") {
                return null;
            }
            country = "中国";
        } else {
            country = geo;
        }

        MobileResult mobileResult = new MobileResult();
        mobileResult.setServiceProvider(ServiceProviderEnum.fetch(PhoneUtil.getCarrier(countryCode, mobile)));
        mobileResult.setCountry(country);
        mobileResult.setProvince(provice);
        mobileResult.setCity(city);
        mobileResult.setFetchFrom(this.getClass().getSimpleName());

        return mobileResult;
    }

}
