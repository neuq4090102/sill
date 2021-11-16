package com.elv.core.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Optional;

/**
 * Date and Time generator
 *
 * @author lxh
 * @since 2020-03-23
 */
public class Dater {

    private ZonedDateTime zonedDateTime;

    private Dater() {
    }

    private static Dater of() { // 内部使用
        return new Dater();
    }

    public static Dater of(Date date) {
        return of(date.getTime() + "", now().zoneId());
    }

    public static Dater of(String dateStr) {
        return of(dateStr, now().zoneId());
    }

    public static Dater of(String dateStr, int timeZone) {
        return of(dateStr, zoneId(timeZone));
    }

    private static Dater of(String dateTimeStr, ZoneId zoneId) {
        String dateTime = Optional.ofNullable(dateTimeStr).orElse("");
        ZoneId zone = Optional.ofNullable(zoneId).orElse(now().zoneId());
        ZonedDateTime zonedDateTime = null;
        if (dateTime.length() == 0) { // 空对象：当前时间
            zonedDateTime = ZonedDateTime.now();
        } else if (dateTime.length() == 10 && StrUtil.isDigit(dateTime)) { // 时间戳：秒
            zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(dateTime)), zone);
        } else if (dateTime.length() == 13 && StrUtil.isDigit(dateTime)) { // 时间戳：毫秒
            zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(dateTime)), zone);
        } else if (dateTime.length() == 7 && DateUtil.isYearMonth(dateTime)) { // 格式:yyyy-MM
            zonedDateTime = ZonedDateTime
                    .of(LocalDate.parse(dateTime + "-01", DateUtil.DATE_FORMATTER), LocalTime.MIN, zone);
        } else if (dateTime.length() == 10 && DateUtil.isDate(dateTime)) { // 格式:yyyy-MM-dd
            zonedDateTime = ZonedDateTime.of(LocalDate.parse(dateTime, DateUtil.DATE_FORMATTER), LocalTime.MIN, zone);
        } else if (dateTime.length() == 16 && DateUtil.isLongHourMinute(dateTime)) { // 格式:yyyy-MM-dd HH:mm
            zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(dateTime + ":00", DateUtil.DATETIME_FORMATTER), zone);
        } else if (dateTime.length() == 19 && DateUtil.isDateTime(dateTime)) { // 格式:yyyy-MM-dd HH:mm:ss
            zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(dateTime, DateUtil.DATETIME_FORMATTER), zone);
        }

        if (zonedDateTime != null) {
            return Dater.of().zonedDateTime(zonedDateTime);
        }

        throw new IllegalArgumentException("日期参数无法识别：" + dateTimeStr);
    }

    public static Dater of(LocalDate localDate) {
        return Dater.of(localDate, now().zoneId());
    }

    public static Dater of(LocalDate localDate, ZoneId zoneId) {
        return now().zonedDateTime(ZonedDateTime.of(localDate, LocalTime.MIN, zoneId));
    }

    public static Dater of(LocalDateTime localDateTime) {
        return Dater.of(localDateTime, Dater.now().zoneId());
    }

    public static Dater of(LocalDateTime localDateTime, ZoneId zoneId) {
        return Dater.now().zonedDateTime(ZonedDateTime.of(localDateTime, zoneId));
    }

    public static Dater now() {
        return Dater.of().zonedDateTime(ZonedDateTime.now());
    }

    public static Dater now(int timeZone) {
        return Dater.of().zonedDateTime(ZonedDateTime.now(zoneId(timeZone)));
    }

    public static Dater now(ZoneId timeZone) {
        return Dater.of().zonedDateTime(ZonedDateTime.now(timeZone));
    }

    /**
     * 获取时区对象
     * <p>timeZone的取值范围：[-12, 12],其中-12和12表示同一时区</p>
     *
     * @param timeZone 时区
     * @return java.time.ZoneId
     */
    public static ZoneId zoneId(int timeZone) {
        if (timeZone > 12) {
            timeZone = 12;
        } else if (timeZone < -12) {
            timeZone = -12;
        }

        if (timeZone >= 0) {
            return ZoneId.of("+" + timeZone);
        } else {
            return ZoneId.of("" + timeZone);
        }
    }

    public Dater zonedDateTime(ZonedDateTime zonedDateTime) {
        this.setZonedDateTime(zonedDateTime);
        return this;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    // ***********************************Dater application.**************************************

    public Dater year(int year) {
        return this.zonedDateTime(this.getZonedDateTime().withYear(year));
    }

    public Dater month(int month) {
        return this.zonedDateTime(this.getZonedDateTime().withMonth(month));
    }

    public Dater dayOfMonth(int dayOfMonth) {
        return this.zonedDateTime(this.getZonedDateTime().withDayOfMonth(dayOfMonth));
    }

    public Dater dayOfYear(int dayOfYear) {
        return this.zonedDateTime(this.getZonedDateTime().withDayOfYear(dayOfYear));
    }

    public Dater hour(int hour) {
        return this.zonedDateTime(this.getZonedDateTime().withHour(hour));
    }

    public Dater minute(int minute) {
        return this.zonedDateTime(this.getZonedDateTime().withMinute(minute));
    }

    public Dater second(int second) {
        return this.zonedDateTime(this.getZonedDateTime().withSecond(second));
    }

    public int year() {
        return this.getZonedDateTime().getYear();
    }

    public int month() {
        return this.getZonedDateTime().getMonthValue();
    }

    public int day() {
        return this.dayOfYear();
    }

    public int dayOfYear() {
        return this.getZonedDateTime().getDayOfYear();
    }

    public int dayOfMonth() {
        return this.getZonedDateTime().getDayOfMonth();
    }

    public int dayOfWeek() {
        return this.getZonedDateTime().getDayOfWeek().getValue();
    }

    public int hour() {
        return this.getZonedDateTime().getHour();
    }

    public int minute() {
        return this.getZonedDateTime().getMinute();
    }

    public int second() {
        return this.getZonedDateTime().getSecond();
    }

    /**
     * 当前日期按年数偏移
     *
     * @param years 年数
     * @return com.elv.core.util.Dater
     */
    public Dater offsetYears(long years) {
        return this.zonedDateTime(this.getZonedDateTime().plusYears(years));
    }

    /**
     * 当前日期按月份数偏移
     *
     * @param months 月数
     * @return com.elv.core.util.Dater
     */
    public Dater offsetMonths(long months) {
        return this.zonedDateTime(this.getZonedDateTime().plusMonths(months));
    }

    /**
     * 当前日期按天数偏移
     *
     * @param days 天数
     * @return com.elv.core.util.Dater
     */
    public Dater offsetDays(long days) {
        return this.zonedDateTime(this.getZonedDateTime().plusDays(days));
    }

    /**
     * 当前日期按星期数偏移
     *
     * @param weeks 星期数
     * @return com.elv.core.util.Dater
     */
    public Dater offsetWeeks(long weeks) {
        return this.zonedDateTime(this.getZonedDateTime().plusWeeks(weeks));
    }

    /**
     * 当前日期按小时数偏移
     *
     * @param hours 小时数
     * @return com.elv.core.util.Dater
     */
    public Dater offsetHours(long hours) {
        return this.zonedDateTime(this.getZonedDateTime().plusHours(hours));
    }

    /**
     * 当前日期按分钟数偏移
     *
     * @param minutes 分钟数
     * @return com.elv.core.util.Dater
     */
    public Dater offsetMinutes(long minutes) {
        return this.zonedDateTime(this.getZonedDateTime().plusMinutes(minutes));
    }

    /**
     * 当前日期按秒数偏移
     *
     * @param seconds 秒数
     * @return com.elv.core.util.Dater
     */
    public Dater offsetSeconds(long seconds) {
        return this.zonedDateTime(this.getZonedDateTime().plusSeconds(seconds));
    }

    /**
     * 年份间隔
     *
     * @param dater 日期对象
     * @return int
     */
    public int yearsBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getYears());
    }

    /**
     * 月份间隔
     *
     * @param dater 日期对象
     * @return int
     */
    public int monthsBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getMonths());
    }

    /**
     * 天数间隔
     *
     * @param dater 日期对象
     * @return int
     */
    public int daysBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getDays());
    }

    /**
     * 当前日期开始时间
     *
     * @return Dater
     */
    public Dater start() {
        return this.zonedDateTime(ZonedDateTime.of(this.localDate(), LocalTime.MIN, this.zoneId()));
    }

    /**
     * 当前日期结束时间
     *
     * @return Dater
     */
    public Dater end() {
        return this.zonedDateTime(ZonedDateTime.of(this.localDate(), LocalTime.MAX, this.zoneId()));
    }

    /**
     * 当前日期中午时间
     *
     * @return Dater
     */
    public Dater noon() {
        return this.zonedDateTime(ZonedDateTime.of(this.localDate(), LocalTime.NOON, this.zoneId()));
    }

    /**
     * 当前日期以小时数为hour开始的时间点
     * <pre>
     *  Dater.of("2020-01-01 12:23:24").startOf(4)  =  2020-01-01 04:00:00
     * </pre>
     *
     * @param hour 小时
     * @return com.elv.core.util.Dater
     */
    public Dater startOf(int hour) {
        if (hour < 0 || hour > 23) {
            hour = 0;
        }
        return this.zonedDateTime(ZonedDateTime.of(this.localDate(), LocalTime.of(hour, 0), this.zoneId()));
    }

    /**
     * 当前日期以小时数为hour结尾的时间点
     * <pre>
     *  Dater.of("2020-01-01 12:23:24").endOf(24)  =  2020-01-01 23:59:59
     *  Dater.of("2020-01-01 12:23:24").endOf(0)   =  2019-12-31 23:59:59
     * </pre>
     *
     * @param hour 小时
     * @return com.elv.core.util.Dater
     */
    public Dater endOf(int hour) {
        int dayOffset = 0;
        if (hour <= 0) {
            hour = 24;
            dayOffset = -1;
        } else if (hour > 23) {
            hour = 24;
        }

        return this.zonedDateTime(ZonedDateTime
                .of(this.offsetDays(dayOffset).localDate(), LocalTime.of(hour - 1, 0, 0).plusHours(1).minusNanos(1L),
                        this.zoneId()));
    }

    /**
     * 当前日期所在年初
     *
     * @return com.elv.core.util.Dater
     */
    public Dater firstDayOfYear() {
        return this.zonedDateTime(this.getZonedDateTime().with(TemporalAdjusters.firstDayOfYear()));
    }

    /**
     * 当前日期所在年末
     *
     * @return com.elv.core.util.Dater
     */
    public Dater lastDayOfYear() {
        return this.zonedDateTime(this.getZonedDateTime().with(TemporalAdjusters.lastDayOfYear()));
    }

    /**
     * 当前日期所在月初
     *
     * @return com.elv.core.util.Dater
     */
    public Dater firstDayOfMonth() {
        return this.zonedDateTime(this.getZonedDateTime().with(TemporalAdjusters.firstDayOfMonth()));
    }

    /**
     * 当前日期所在月末
     *
     * @return com.elv.core.util.Dater
     */
    public Dater lastDayOfMonth() {
        return this.zonedDateTime(this.getZonedDateTime().with(TemporalAdjusters.lastDayOfMonth()));
    }

    /**
     * 在之前(-∞,dater)
     *
     * @param dater 日期对象
     * @return boolean
     */
    public boolean before(Dater dater) {
        return this.getZonedDateTime().isBefore(dater.getZonedDateTime());
    }

    /**
     * 在之后(dater,+∞)
     *
     * @param dater 日期对象
     * @return boolean
     */
    public boolean after(Dater dater) {
        return this.getZonedDateTime().isAfter(dater.getZonedDateTime());
    }

    /**
     * 在之间[startDater, endDater]
     *
     * @param startDater 起始日期
     * @param endDater   结束日期
     * @return boolean
     */
    public boolean between(Dater startDater, Dater endDater) {
        return !startDater.after(endDater) && !this.after(endDater) && !this.before(startDater);
    }

    /**
     * 是否是闰年
     *
     * @return boolean
     */
    public boolean isLeapYear() {
        return this.getZonedDateTime().toLocalDate().isLeapYear();
    }

    /**
     * 获取日期+时间
     *
     * @return java.time.LocalDateTime
     */
    public LocalDateTime localDateTime() {
        return this.getZonedDateTime().toLocalDateTime();
    }

    /**
     * 获取日期
     *
     * @return java.time.LocalDate
     */
    public LocalDate localDate() {
        return this.getZonedDateTime().toLocalDate();
    }

    /**
     * 获取时间
     *
     * @return java.time.LocalTime
     */
    public LocalTime localTime() {
        return this.getZonedDateTime().toLocalTime();
    }

    /**
     * 获取日期
     *
     * @return java.util.Date
     */
    public Date date() {
        return Date.from(this.getZonedDateTime().toInstant());
    }

    /**
     * 获取时间戳对象
     *
     * @return java.time.Instant
     */
    public Instant instant() {
        return this.getZonedDateTime().toInstant();
    }

    /**
     * 获取时间戳（单位：毫秒）
     *
     * @return long
     */
    public long ts() {
        return timestamp();
    }

    public long timestamp() {
        return this.getZonedDateTime().toInstant().toEpochMilli();
    }

    /**
     * 获取当前时区
     *
     * @return java.time.ZoneId
     */
    public ZoneId zoneId() {
        return this.getZonedDateTime().getZone();
    }

    /**
     * 获取字符串日期+时间，格式：yyyy-MM-dd HH:mm:ss
     *
     * @return java.lang.String
     */
    public String dateTimeStr() {
        return this.formatOf(DateUtil.DATETIME_FORMATTER);
    }

    /**
     * 获取字符串日期，格式：yyyy-MM-dd
     *
     * @return java.lang.String
     */
    public String dateStr() {
        return this.formatOf(DateUtil.DATE_FORMATTER);
    }

    /**
     * 获取字符串时间，格式：HH:mm:ss
     *
     * @return java.lang.String
     */
    public String timeStr() {
        return this.formatOf(DateUtil.TIME_FORMATTER);
    }

    /**
     * 获取字符串年月，格式：yyyy-MM
     *
     * @return java.lang.String
     */
    public String yearMonthStr() {
        return this.formatOf(DateUtil.YEAR_MONTH_FORMATTER);
    }

    /**
     * 获取固定格式日期字符串
     *
     * @param formatter
     * @return java.lang.String
     */
    public String formatOf(DateTimeFormatter formatter) {
        return this.getZonedDateTime().format(formatter);
    }

    /**
     * 获取月份枚举名称
     *
     * @return java.lang.String
     */
    public String monthName() {
        return this.getZonedDateTime().getMonth().name();
    }

    /**
     * 获取星期枚举名称
     *
     * @return java.lang.String
     */
    public String weekName() {
        return this.getZonedDateTime().getDayOfWeek().name();
    }

    @Override
    public String toString() {
        return this.dateTimeStr();
    }

    public static void main(String[] args) {
        System.out.println(Dater.now().instant().toEpochMilli());

        System.out.println(Dater.of("2019-12", 7).dateTimeStr());
        System.out.println(Dater.of("2019-12-31", 7));
        System.out.println(Dater.of("2019-12-31 12:23:24", 7));
        System.out.println(Dater.of("1585651508506", 7));
        System.out.println(Dater.of("1585651508", 7));

        System.out.println("start>>>" + Dater.of("1585651508", 7).start());
        System.out.println("startOf>>>" + Dater.of("2019-12-31 12:23:24", 7).startOf(4));
        System.out.println("startOf>>>" + Dater.of("1585651508", 7).startOf(14));
        System.out.println("startOf>>>" + Dater.of("1585651508", 7).startOf(25));
        System.out.println("end>>>" + Dater.of("1585651508", 7).end());
        System.out.println("endOf>>>" + Dater.of("1585651508", 7).endOf(4));
        System.out.println("endOf>>>" + Dater.of("2019-12-31 12:23:24", 7).endOf(24));
        System.out.println("endOf>>>" + Dater.of("2019-12-31 12:23:24", 7).endOf(0));
        System.out.println("endOf>>>" + Dater.of("1585651508", 7).endOf(28));
        System.out.println("endOf>>>" + Dater.of("1585651508", 7).endOf(8));
        System.out.println("noon>>>" + Dater.of("1585651508", 7).noon());

        System.out.println(Dater.now().isLeapYear());

        System.out.println(Dater.now().offsetYears(2));
        System.out.println(Dater.now().offsetYears(-2));
        System.out.println(Dater.now().offsetMonths(2));
        System.out.println(Dater.now().offsetMonths(-2));
        System.out.println(Dater.now().offsetDays(2));
        System.out.println(Dater.now().offsetDays(-2));
        System.out.println(Dater.now().offsetWeeks(2));
        System.out.println(Dater.now().offsetWeeks(-2));
        System.out.println(Dater.now().offsetHours(2));
        System.out.println(Dater.now().offsetHours(-2));
        System.out.println(Dater.now().offsetMinutes(2));
        System.out.println(Dater.now().offsetMinutes(-2));
        System.out.println(Dater.now().offsetSeconds(2));
        System.out.println(Dater.now().offsetSeconds(-2));

        System.out.println("first&last>>>" + Dater.now().firstDayOfYear());
        System.out.println("first&last>>>" + Dater.now().lastDayOfYear());
        System.out.println("first&last>>>" + Dater.now().firstDayOfMonth());
        System.out.println("first&last>>>" + Dater.now().lastDayOfMonth());

        System.out.println(Dater.now().year());
        System.out.println(Dater.now().month());
        System.out.println(Dater.now().dayOfYear());
        System.out.println(Dater.now().dayOfMonth());
        System.out.println(Dater.now().dayOfWeek());
        System.out.println(Dater.now().hour());
        System.out.println(Dater.now().minute());
        System.out.println(Dater.now().second());

        System.out.println(Dater.now().monthName());
        System.out.println(Dater.now().weekName());

        System.out.println("between>>>" + Dater.now().yearsBetween(Dater.of("2021-04-01 00:23:22")));
        System.out.println("between>>>" + Dater.now().monthsBetween(Dater.of("2020-03-01 00:23:22")));
        System.out.println("between>>>" + Dater.now().daysBetween(Dater.of("2020-03-29 00:23:22")));

        System.out.println(Dater.of("2020-01-01 12:23:24").endOf(24));
        System.out.println(Dater.of("2020-01-01 12:23:24").endOf(0));

        System.out.println(Dater.now(8));
        System.out.println(Dater.now(7));

        Dater ref = Dater.now();
        Dater start = Dater.of(ref.instant().toEpochMilli() + "").offsetSeconds(1);
        Dater end = Dater.of(ref.instant().toEpochMilli() + "").offsetHours(1);
        System.out.println(ref.instant());
        System.out.println(start.instant());
        System.out.println(end.instant());
        System.out.println(ref.between(start, end));
        System.out.println(ref.between(ref, ref));

        System.out.println(Dater.now().offsetDays(-1).end().ts());
        System.out.println(Dater.now().endOf(-1).ts());

        System.out.println(Dater.now().year(2021).month(2).dayOfMonth(28).hour(22).minute(33).second(33));

        System.out.println(Dater.now().dateStr());
        System.out.println(Dater.now().timeStr());
        System.out.println(Dater.now().dateTimeStr());
        System.out.println(Dater.now().yearMonthStr());
        System.out.println(Dater.of("2020-11-12 15:33"));
        System.out.println(Dater.now().ts());
        System.out.println(Dater.of("1615185816945", 8));
        System.out.println(Dater.of("1615185816945", 7));

    }

}
