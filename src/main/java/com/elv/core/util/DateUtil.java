package com.elv.core.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.elv.core.constant.Const;
import com.elv.core.constant.FormEnum.DateForm;
import com.elv.core.constant.SortEnum;

/**
 * Date and Time util
 *
 * @author lxh
 * @since 2020-03-23
 */
public class DateUtil {

    public static final DateTimeFormatter DATE_FORMATTER;
    public static final DateTimeFormatter TIME_FORMATTER;
    public static final DateTimeFormatter DATETIME_FORMATTER;
    public static final DateTimeFormatter YEAR_MONTH_FORMATTER;

    static {
        DATE_FORMATTER = DateTimeFormatter.ofPattern(DateForm.DATE.getPattern());
        TIME_FORMATTER = DateTimeFormatter.ofPattern(DateForm.TIME.getPattern());
        DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DateForm.DATE_TIME.getPattern());
        YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern(DateForm.YEAR_MONTH.getPattern());
    }

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

    /**
     * 是否是短时分格式：HH:mm
     *
     * @param hourMinute
     * @return boolean
     */
    public static boolean isShortHourMinute(String hourMinute) {
        return check(Optional.ofNullable(hourMinute).orElse("") + ":00", DateForm.TIME);
    }

    /**
     * 是否是长时分格式：yyyy-MM-dd HH:mm
     *
     * @param hourMinute
     * @return boolean
     */
    public static boolean isLongHourMinute(String hourMinute) {
        return check(Optional.ofNullable(hourMinute).orElse("") + ":00", DateForm.DATE_TIME);
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

    /**
     * 生日
     *
     * @param date
     * @return com.elv.core.util.Dater
     */
    public static Dater birthday(String date) {
        if (!DateUtil.isDate(date) && !DateUtil.isLongHourMinute(date) && !DateUtil.isDateTime(date)) {
            return Dater.now();
        }
        Dater dater = Dater.of(date);
        LocalDate localDate = LocalDate.of(Dater.now().year(), dater.month(), dater.dayOfMonth());
        return Dater.of(localDate, dater.zoneId());
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
                    .sorted(Comparator.comparing(item -> item.ts(), comparator)).findFirst().get().dateTimeStr();
        } else if (allIsDate(dateTimes)) {
            return Arrays.stream(dateTimes).map(item -> Dater.of(item))
                    .sorted(Comparator.comparing(item -> item.ts(), comparator)).findFirst().get().dateStr();
        } else if (allIsTime(dateTimes)) {
            Dater now = Dater.now();
            return Arrays.stream(dateTimes).map(item -> Dater.of(now.dateStr() + " " + item))
                    .sorted(Comparator.comparing(item -> item.ts(), comparator)).findFirst().get().timeStr();
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
        return Arrays.stream(dateTimes).sorted(Comparator.comparing(item -> item.ts(), comparator)).findFirst().get();
    }

    /**
     * 获取开始日期
     * <p>
     * 1.默认都是以【当年的一月一日】为参考开始日期，eg:2021-01-01
     * 2.dateUnit为天，并无意义；dateUnit为周，参数refStartDate失效
     *
     * <pre>
     *  fetchStartDater("2021-11-28", null, month)             -> 2021-11-01
     *  fetchStartDater("2021-11-27", "2021-10-28", month)     -> 2021-10-28
     *  fetchStartDater("2021-11-28", "2021-10-28", month)     -> 2021-11-28
     *
     *  fetchStartDater("2021-01-28", null, quarter)           -> 2021-01-01
     *  fetchStartDater("2021-01-28", "2021-01-25", quarter)   -> 2021-01-25
     *  fetchStartDater("2021-04-24", "2021-01-25", quarter)   -> 2021-01-25
     *  fetchStartDater("2021-04-25", "2021-01-25", quarter)   -> 2021-04-25
     *
     *  fetchStartDater("2021-05-03", null, year)              -> 2021-01-01
     *  fetchStartDater("2021-05-03", "2021-06-01", year)      -> 2020-06-01
     *  fetchStartDater("2022-05-03", "2021-06-01", year)      -> 2021-06-01
     *  fetchStartDater("2022-08-03", "2021-06-01", year)      -> 2022-06-01
     *  fetchStartDater("2023-05-22", "2021-06-01", year)      -> 2022-06-01
     * </pre>
     *
     * @param businessDate 营业日
     * @param refStartDate 指定开始日期
     * @param dateUnit     日期单位
     * @return com.navimind.utils.DateFormater
     */
    public static Dater fetchStartDater(String businessDate, String refStartDate, DateUnitEnum dateUnit) {
        Dater bdf = Dater.of(businessDate, Const.TIME_ZONE);
        String dfStr = refStartDate;
        if (StringUtils.isBlank(refStartDate)) {
            dfStr = bdf.year() + "-01-01";
        }
        Dater df = Dater.of(dfStr, Const.TIME_ZONE);
        while (true) {
            if (dateUnit == DateUnitEnum.WEEK) {
                return bdf.offsetDays(-bdf.dayOfWeek() + 1);
            } else if (DateUnitEnum.belongMonthlyPeriod(dateUnit)) {
                int period = dateUnit.getPeriod();
                df = df.offsetMonths(period);
                if (df.after(bdf)) {
                    while (true) {
                        if (!df.after(bdf)) {
                            return df;
                        } else {
                            df = df.offsetMonths(-period);
                        }
                    }
                }
            } else {
                return bdf;
            }
        }
    }

    /**
     * 日期单位
     */
    public enum DateUnitEnum {
        DAY(1, "天", 1), // 1天
        WEEK(2, "周", 7), // 7天
        MONTH(3, "月", 1), // 1个月
        QUARTER(4, "季度", 3), // 3个月
        YEAR(5, "年", 12), // 12个月
        ;

        final private int key;
        final private String desc;
        final private int period; // 周期

        private static Map<Integer, DateUnitEnum> map = new HashMap<>();

        static {
            for (DateUnitEnum item : DateUnitEnum.values()) {
                map.put(item.getKey(), item);
            }
        }

        DateUnitEnum(int key, String desc, int period) {
            this.key = key;
            this.desc = desc;
            this.period = period;
        }

        public int getKey() {
            return key;
        }

        public String getDesc() {
            return desc;
        }

        public int getPeriod() {
            return period;
        }

        public static DateUnitEnum itemOf(int key) {
            return map.get(key);
        }

        public static boolean belongMonthlyPeriod(DateUnitEnum dateUnit) {
            return dateUnit == MONTH || dateUnit == QUARTER || dateUnit == YEAR;
        }

    }

    public static void main(String[] args) {
        // DateUtil dateUtil = new DateUtil();
        // System.out.println(Integer.valueOf("0004"));
        // System.out.println(isDate("2019-09-30"));
        // System.out.println(isTime("23:59:59"));
        // System.out.println(isTime("24:00:00"));
        // System.out.println(isTime("00:00:00"));
        // System.out.println(isDateTime("2019-09-30 15:59:59"));
        // System.out.println(isYearMonth("2019-04"));
        //
        // System.out.println(Dater.now().formatOf(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // System.out.println(Dater.now().formatOf(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        // System.out.println(Dater.now().formatOf(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+0700'")));
        //
        // System.out.println(min("00:00:00", "24:00:00"));
        // System.out.println(max("00:00:00", "24:00:00"));
        // System.out.println(min("2020-10-11", "2020-11-11", "2020-11-15"));
        // System.out.println(max("2020-10-11", "2020-11-11", "2020-11-15"));
        // System.out.println(max("2020-11-10 12:11:11", "2020-10-11"));

        // System.out.println(birthday("1990-11-19 11:12").dateStr());

        String nowStr = Dater.now().dateStr();
        String refDate = "2021-10-28";

        DateUnitEnum day = DateUnitEnum.DAY;
        DateUnitEnum week = DateUnitEnum.WEEK;
        DateUnitEnum month = DateUnitEnum.MONTH;
        DateUnitEnum quarter = DateUnitEnum.QUARTER;
        DateUnitEnum year = DateUnitEnum.YEAR;

        System.out.println(day.getDesc() + ":" + fetchStartDater(null, null, null));
        System.out.println(day.getDesc() + ":" + fetchStartDater(nowStr, null, null));
        System.out.println(day.getDesc() + ":" + fetchStartDater(nowStr, "2021-09-28", null));
        System.out.println(day.getDesc() + ":" + fetchStartDater(nowStr, "2021-09-28", day));
        System.out.println(day.getDesc() + ":" + fetchStartDater("2021-09-28", refDate, day));
        System.out.println(day.getDesc() + ":" + fetchStartDater("2021-09-28", refDate, day));
        System.out.println();

        System.out.println(week.getDesc() + ":" + fetchStartDater(nowStr, null, week));
        System.out.println(week.getDesc() + ":" + fetchStartDater(nowStr, refDate, week));
        System.out.println();
        System.out.println(month.getDesc() + ":" + fetchStartDater(nowStr, refDate, month));
        System.out.println(month.getDesc() + ":" + fetchStartDater("2021-11-28", null, month));
        System.out.println(month.getDesc() + ":" + fetchStartDater("2021-11-27", "2021-10-28", month));
        System.out.println(month.getDesc() + ":" + fetchStartDater("2021-11-28", "2021-10-28", month));
        System.out.println(month.getDesc() + ":" + fetchStartDater("2020-02-27", "", month));

        System.out.println();
        System.out.println(quarter.getDesc() + ":" + fetchStartDater(nowStr, null, quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-01-28", null, quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-01-28", "2021-01-25", quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-04-24", "2021-01-25", quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-04-25", "2021-01-25", quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-02-28", null, quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-03-28", null, quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-06-28", null, quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-12-28", null, quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2022-12-28", "2021-01-02", quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2021-12-28", "2022-01-02", quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater("2022-01-01", "", quarter));
        System.out.println(quarter.getDesc() + ":" + fetchStartDater(nowStr, refDate, quarter));
        System.out.println();
        System.out.println(year.getDesc() + ":" + fetchStartDater(nowStr, null, year));
        System.out.println(year.getDesc() + ":" + fetchStartDater(nowStr, refDate, year));
        System.out.println(year.getDesc() + ":" + fetchStartDater(nowStr, "2021-06-01", year));

        System.out.println(year.getDesc() + ":" + fetchStartDater("2021-05-03", null, year));
        System.out.println(year.getDesc() + ":" + fetchStartDater("2020-05-03", "2021-06-03", year));
        System.out.println(year.getDesc() + ":" + fetchStartDater("2022-05-03", "2021-06-03", year));
        System.out.println(year.getDesc() + ":" + fetchStartDater("2022-06-03", "2021-06-03", year));
        System.out.println(year.getDesc() + ":" + fetchStartDater("2022-08-03", "2021-06-03", year));
        System.out.println(year.getDesc() + ":" + fetchStartDater("2023-05-22", "2021-06-03", year));
        System.out.println(year.getDesc() + ":" + fetchStartDater("2023-06-02", "2021-06-03", year));
    }
}
