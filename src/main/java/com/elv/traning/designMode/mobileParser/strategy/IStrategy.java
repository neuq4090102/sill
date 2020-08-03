package com.elv.traning.designMode.mobileParser.strategy;

import com.elv.core.constant.Const;
import com.elv.core.util.StrUtil;
import com.elv.traning.designMode.mobileParser.MobileResult;

/**
 * @author lxh
 * @since 2020-06-29
 */
public interface IStrategy {

    /**
     * 若不使用某个策略，改成false即可
     *
     * @return boolean
     * @author lxh
     * @since 2020-07-30
     */
    boolean enable();

    MobileResult fetch(int countryCode, String mobile);

    default MobileResult defaultFetch(String mobile) {
        if (!enable()) {
            return null;
        }

        String[] mobileArr = mobile.split("-");
        int countryCode = Const.COUNTRY_CODE_CHINA;
        if (mobileArr.length == 2) {
            if (!StrUtil.isDigit(mobileArr[0])) {
                return null;
            }
            countryCode = Integer.valueOf(mobileArr[0]);
            mobile = mobileArr[1];
        } else {
            mobile = mobileArr[0];
        }

        if (!StrUtil.isDigit(mobile)) {
            return null;
        }

        return fetch(countryCode, mobile);
    }
}
