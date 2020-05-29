package com.elv.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_YM = "yyyy-MM";

    public static final String PATTERN_YMD = "\\d{4}-\\d{2}-\\d{2}";
    public static final String PATTERN_YM = "\\d{4}-\\d{2}";

    private DateUtil() {
    }

    /**
     * 是否是日期格式：yyyy-MM-dd
     *
     * @param dates
     * @return
     */
    public static boolean isDate(String... dates) {
        for (String date : dates) {
            if (!isDate(date)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 是否是日期格式：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }
        Pattern p = Pattern.compile(PATTERN_YMD);
        Matcher m = p.matcher(date);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 是否是年月格式：yyyy-MM
     *
     * @param date
     * @return
     */
    public static boolean isYearMonth(String date) {
        if (StringUtils.isEmpty(date)) {
            return false;
        }
        Pattern p = Pattern.compile(PATTERN_YM);
        Matcher m = p.matcher(date);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isDate("3019-11-33", "2019-11-33", "3019-11-33"));
        System.out.println(isDate("3019-11-33", "2019-11-33"));
        System.out.println(isYearMonth("3019-11"));
    }
}
