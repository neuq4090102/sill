package com.elv.core.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.elv.core.constant.Const;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

/**
 * @author lxh
 * @since 2020-06-16
 */
public class PhoneUtil {

    private PhoneUtil() {

    }

    public static boolean isPhone(String phone) {
        return GooglePhoneUtil.isPhone(Const.COUNTRY_CODE_CHINA, Long.valueOf(phone));
    }

    public static boolean isPhone(int countryCode, String phone) {
        return GooglePhoneUtil.isPhone(countryCode, Long.valueOf(phone));
    }

    public static String getCarrier(String phone) {
        return GooglePhoneUtil.getCarrier(Const.COUNTRY_CODE_CHINA, Long.valueOf(phone));
    }

    public static String getCarrier(int countryCode, String phone) {
        return GooglePhoneUtil.getCarrier(countryCode, Long.valueOf(phone));
    }

    public static String getGeo(String phone) {
        return GooglePhoneUtil.getGeo(Const.COUNTRY_CODE_CHINA, Long.valueOf(phone));
    }

    public static String getGeo(int countryCode, String phone) {
        return GooglePhoneUtil.getGeo(countryCode, Long.valueOf(phone));
    }

    public static List<String> getMunicipalities() {
        return Arrays.asList(GooglePhoneUtil.MUNICIPALITY);
    }

    public static List<String> getAutonomousRegions() {
        return Arrays.asList(GooglePhoneUtil.AUTONOMOUS_REGION);
    }

    private static class GooglePhoneUtil {

        /**
         * 直辖市
         */
        private final static String[] MUNICIPALITY = { "北京市", "天津市", "上海市", "重庆市" };

        /**
         * 自治区
         */
        private final static String[] AUTONOMOUS_REGION = { "新疆", "内蒙古", "西藏", "宁夏", "广西" };

        private static PhoneNumberUtil util; // 工具
        private static PhoneNumberToCarrierMapper carrierMapper; // 服务提供商
        private static PhoneNumberOfflineGeocoder geocoder; // 地理位置

        static {
            util = PhoneNumberUtil.getInstance();
            carrierMapper = PhoneNumberToCarrierMapper.getInstance();
            geocoder = PhoneNumberOfflineGeocoder.getInstance();
        }

        private GooglePhoneUtil() {

        }

        private static boolean isPhone(int countryCode, long phone) {
            PhoneNumber pn = new PhoneNumber();
            pn.setCountryCode(countryCode);
            pn.setNationalNumber(phone);
            return util.isValidNumber(pn);
        }

        private static String getCarrier(int countryCode, long phone) {
            PhoneNumber pn = new PhoneNumber();
            pn.setCountryCode(countryCode);
            pn.setNationalNumber(phone);
            return carrierMapper.getNameForNumber(pn, Locale.CHINA);
        }

        private static String getGeo(int countryCode, long phone) {
            PhoneNumber pn = new PhoneNumber();
            pn.setCountryCode(countryCode);
            pn.setNationalNumber(phone);
            return geocoder.getDescriptionForNumber(pn, Locale.CHINA);
        }

    }

    public static void main(String[] args) {
        System.out.println(isPhone("05586263084")); // 安徽座机
        System.out.println(isPhone("01056673266")); // 北京座机
        System.out.println(isPhone("18584838281")); // 四川成都手机
        System.out.println(isPhone("17647461382")); // 内蒙古呼市手机
        System.out.println(isPhone("14400000000")); // 非手机号/座机
        System.out.println(isPhone("00000000")); // 非手机号/座机

        System.out.println(getCarrier("05586263084"));
        System.out.println(getCarrier("01056673266"));
        System.out.println(getCarrier("18584838281"));

        System.out.println(getGeo("05586263084"));
        System.out.println(getGeo("01056673266"));
        System.out.println(getGeo("18584838281"));
        System.out.println(getGeo("17614769319"));
        System.out.println(getGeo("13995459353"));
    }

}
