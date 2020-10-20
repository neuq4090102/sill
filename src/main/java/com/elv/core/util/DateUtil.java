package com.elv.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.elv.core.constant.FormEnum;
import com.elv.core.constant.FormEnum.DateForm;

/**
 * Date and Time util
 *
 * @author lxh
 * @since 2020-03-23
 */
public class DateUtil {

    private DateUtil() {
    }

    /**
     * 是否都是日期格式：yyyy-MM-dd
     *
     * @param dates 日期格式字符串
     * @return boolean
     */
    public static boolean allIsDate(String... dates) {
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
     * @param date 日期格式字符串
     * @return boolean
     */
    public static boolean isDate(String date) {
        return check(date, DateForm.DATE);
    }

    /**
     * 是否都是时间格式：HH:mm:ss
     *
     * @param times 时间格式字符串
     * @return boolean
     */
    public static boolean allIsTime(String... times) {
        for (String time : times) {
            if (!isTime(time)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是时间格式：HH:mm:ss
     *
     * @param time 时间格式字符串
     * @return boolean
     */
    public static boolean isTime(String time) {
        return check(time, DateForm.TIME);
    }

    /**
     * 是否都是日期时间格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTimes 日期时间格式字符串
     * @return boolean
     */
    public static boolean allIsDateTime(String... dateTimes) {
        for (String dateTime : dateTimes) {
            if (!isDateTime(dateTime)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是日期时间格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime 日期时间格式字符串
     * @return boolean
     */
    public static boolean isDateTime(String dateTime) {
        return check(dateTime, DateForm.DATE_TIME);
    }

    /**
     * 是否都是年月格式：yyyy-MM
     *
     * @param yearMonths 年月格式字符串
     * @return boolean
     */
    public static boolean allIsYearMonth(String... yearMonths) {
        for (String yearMonth : yearMonths) {
            if (!isYearMonth(yearMonth)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是年月格式：yyyy-MM
     *
     * @param yearMonth 年月格式字符串
     * @return boolean
     */
    public static boolean isYearMonth(String yearMonth) {
        return check(yearMonth + "-01", DateForm.DATE);
    }

    private static boolean check(String str, DateForm dateForm) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        Pattern p = Pattern.compile(dateForm.getRegex());
        Matcher m = p.matcher(str);
        if (!m.matches()) {
            return false;
        }

        String[] groups = str.split(" ");
        if (DateForm.hasDate(dateForm)) {
            String[] splits = groups[0].split("-");
            int year = Integer.valueOf(splits[0]);
            int month = Integer.valueOf(splits[1]);
            int day = Integer.valueOf(splits[2]);

            if (month < 1 || month > 12) {
                return false;
            } else if (day < 1 || day > 31) {
                return false;
            }
            if (month == 2) {
                boolean leapYear = isLeapYear(year);
                if (leapYear && day > 29) {
                    return false;
                } else if (!leapYear && day > 28) {
                    return false;
                }
            } else if (Stream.of(4, 6, 9, 11).collect(Collectors.toList()).contains(month) && day > 30) {
                return false;
            }
        }

        if (DateForm.hasTime(dateForm)) {
            String[] splits = null;
            if (dateForm == DateForm.TIME) {
                splits = groups[0].split(":");
            } else if (dateForm == DateForm.DATE_TIME) {
                splits = groups[1].split(":");
            }
            if (splits == null) {
                return false;
            }
            int hour = Integer.valueOf(splits[0]);
            int minute = Integer.valueOf(splits[1]);
            int second = Integer.valueOf(splits[2]);
            if (hour < 0 || hour > 23) {
                return false;
            } else if (minute < 0 || minute > 59) {
                return false;
            } else if (second < 0 || second > 59) {
                return false;
            }
        }

        return true;
    }

    /**
     * 是否是闰年
     *
     * @param year 年份
     * @return boolean
     */
    public static boolean isLeapYear(int year) {
        return Dater.of(year + "-01-01").isLeapYear();
    }

    public static void main(String[] args) {
        System.out.println(Integer.valueOf("0004"));
        System.out.println(isDate("2019-09-30"));
        System.out.println(isTime("23:59:59"));
        System.out.println(isTime("24:00:00"));
        System.out.println(isTime("00:00:00"));
        System.out.println(isDateTime("2019-09-30 15:59:59"));
        System.out.println(isYearMonth("2019-04"));
    }
}
