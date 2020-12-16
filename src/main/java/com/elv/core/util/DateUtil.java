package com.elv.core.util;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.elv.core.constant.FormEnum.DateForm;
import com.elv.core.constant.SortEnum;

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
        for (String date : Optional.ofNullable(dates).orElse(new String[] { "" })) {
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
        return check(Optional.ofNullable(date).orElse(""), DateForm.DATE);
    }

    /**
     * 是否都是时间格式：HH:mm:ss
     *
     * @param times 时间格式字符串
     * @return boolean
     */
    public static boolean allIsTime(String... times) {
        for (String time : Optional.ofNullable(times).orElse(new String[] { "" })) {
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
        return check(Optional.ofNullable(time).orElse(""), DateForm.TIME);
    }

    /**
     * 是否都是日期时间格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTimes 日期时间格式字符串
     * @return boolean
     */
    public static boolean allIsDateTime(String... dateTimes) {
        for (String dateTime : Optional.ofNullable(dateTimes).orElse(new String[] { "" })) {
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
        return check(Optional.ofNullable(dateTime).orElse(""), DateForm.DATE_TIME);
    }

    /**
     * 是否都是年月格式：yyyy-MM
     *
     * @param yearMonths 年月格式字符串
     * @return boolean
     */
    public static boolean allIsYearMonth(String... yearMonths) {
        for (String yearMonth : Optional.ofNullable(yearMonths).orElse(new String[] { "" })) {
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
        return check(Optional.ofNullable(yearMonth).orElse("") + "-01", DateForm.DATE);
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

    public static String max(String... dateTimes) {
        if (StrUtil.isAnyBlank(dateTimes)) {
            return "";
        }
        return limitVal(Optional.ofNullable(dateTimes).orElse(new String[] { "" }), SortEnum.DESC);
    }

    public static String min(String... dateTimes) {
        if (StrUtil.isAnyBlank(dateTimes)) {
            return "";
        }
        return limitVal(Optional.ofNullable(dateTimes).orElse(new String[] { "" }), SortEnum.ASC);
    }

    private static String limitVal(String[] dateTimes, SortEnum sort) {
        Comparator<? super Long> comparator = (sort == SortEnum.DESC) ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();

        if (allIsDateTime(dateTimes)) {
            return Arrays.stream(dateTimes).map(item -> Dater.of(item))
                    .sorted(Comparator.comparing(item -> item.getTimestamp(), comparator)).findFirst().get()
                    .getDateTimeStr();
        } else if (allIsDate(dateTimes)) {
            return Arrays.stream(dateTimes).map(item -> Dater.of(item))
                    .sorted(Comparator.comparing(item -> item.getTimestamp(), comparator)).findFirst().get()
                    .getDateStr();
        } else if (allIsTime(dateTimes)) {
            Dater now = Dater.now();
            return Arrays.stream(dateTimes).map(item -> Dater.of(now.getDateStr() + " " + item))
                    .sorted(Comparator.comparing(item -> item.getTimestamp(), comparator)).findFirst().get()
                    .getTimeStr();
        }

        Comparator<String> keyComparator = (sort == SortEnum.DESC) ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();
        return Arrays.stream(dateTimes).sorted(Comparator.comparing(String::toString, keyComparator)).findFirst().get();
    }

    public static Dater max(Dater... dateTimes) {
        return limitVal(Optional.ofNullable(dateTimes).orElse(new Dater[] { Dater.now() }), SortEnum.DESC);
    }

    public static Dater min(Dater... dateTimes) {
        return limitVal(Optional.ofNullable(dateTimes).orElse(new Dater[] { Dater.now() }), SortEnum.ASC);
    }

    private static Dater limitVal(Dater[] dateTimes, SortEnum sort) {
        Comparator<? super Long> comparator = (sort == SortEnum.DESC) ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();
        return Arrays.stream(dateTimes).sorted(Comparator.comparing(item -> item.getTimestamp(), comparator))
                .findFirst().get();
    }

    public static void main(String[] args) {
        System.out.println(Integer.valueOf("0004"));
        System.out.println(isDate("2019-09-30"));
        System.out.println(isTime("23:59:59"));
        System.out.println(isTime("24:00:00"));
        System.out.println(isTime("00:00:00"));
        System.out.println(isDateTime("2019-09-30 15:59:59"));
        System.out.println(isYearMonth("2019-04"));

        System.out.println(Dater.now().getFormatterStr(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println(Dater.now().getFormatterStr(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        System.out.println(Dater.now().getFormatterStr(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+0700'")));

        System.out.println(min("00:00:00", "24:00:00"));
        System.out.println(max("00:00:00", "24:00:00"));
        System.out.println(min("2020-10-11", "2020-11-11", "2020-11-15"));
        System.out.println(max("2020-10-11", "2020-11-11", "2020-11-15"));
        System.out.println(max("2020-11-10 12:11:11", "2020-10-11"));
    }
}
