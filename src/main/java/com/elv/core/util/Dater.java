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
        return of(date.getTime() + "", now().getZoneId());
    }

    public static Dater of(String dateStr) {
        return of(dateStr, now().getZoneId());
    }

    public static Dater of(String dateStr, int timeZone) {
        return of(dateStr, getZoneId(timeZone));
    }

    private static Dater of(String dateTimeStr, ZoneId zoneId) {
        String dateTime = Optional.ofNullable(dateTimeStr).orElse("");
        ZoneId zone = Optional.ofNullable(zoneId).orElse(now().getZoneId());
        ZonedDateTime zonedDateTime = null;
        if (dateTime.length() == 10 && StrUtil.isDigit(dateTime)) { // 时间戳：秒
            zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(dateTime)), zone);
        } else if (dateTime.length() == 13 && StrUtil.isDigit(dateTime)) { // 时间戳：毫秒
            zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(dateTime)), zone);
        } else if (dateTime.length() == 7 && DateUtil.isYearMonth(dateTime)) { // 格式:yyyy-MM
            LocalDate localDate = LocalDate.parse(dateTime + "-01", DateUtil.DATE_FORMATTER);
            zonedDateTime = ZonedDateTime.of(localDate, LocalTime.MIN, zone);
        } else if (dateTime.length() == 10 && DateUtil.isDate(dateTime)) { // 格式:yyyy-MM-dd
            LocalDate localDate = LocalDate.parse(dateTime, DateUtil.DATE_FORMATTER);
            zonedDateTime = ZonedDateTime.of(localDate, LocalTime.MIN, zone);
        } else if (dateTime.length() == 16 && DateUtil.isLongHourMinute(dateTime)) { // 格式:yyyy-MM-dd HH:mm
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime + ":00", DateUtil.DATETIME_FORMATTER);
            zonedDateTime = ZonedDateTime.of(localDateTime, zone);
        } else if (dateTime.length() == 19 && DateUtil.isDateTime(dateTime)) { // 格式:yyyy-MM-dd HH:mm:ss
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateUtil.DATETIME_FORMATTER);
            zonedDateTime = ZonedDateTime.of(localDateTime, zone);
        }

        if (zonedDateTime != null) {
            return Dater.of().zonedDateTime(zonedDateTime);
        }

        throw new IllegalArgumentException("日期参数无法识别：" + dateTimeStr);
    }

    public static Dater of(LocalDate localDate) {
        return Dater.of(localDate, now().getZoneId());
    }

    public static Dater of(LocalDate localDate, ZoneId zoneId) {
        return now().zonedDateTime(ZonedDateTime.of(localDate, LocalTime.MIN, zoneId));
    }

    public static Dater of(LocalDateTime localDateTime) {
        return Dater.of(localDateTime, Dater.now().getZoneId());
    }

    public static Dater of(LocalDateTime localDateTime, ZoneId zoneId) {
        return Dater.now().zonedDateTime(ZonedDateTime.of(localDateTime, zoneId));
    }

    public static Dater now() {
        return Dater.of().zonedDateTime(ZonedDateTime.now());
    }

    public static Dater now(int timeZone) {
        return Dater.of().zonedDateTime(ZonedDateTime.now(getZoneId(timeZone)));
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
    public static ZoneId getZoneId(int timeZone) {
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

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public Dater zonedDateTime(ZonedDateTime zonedDateTime) {
        this.setZonedDateTime(zonedDateTime);
        return this;
    }

    // ***********************************Dater application.**************************************

    public Dater withYear(int year) {
        return this.zonedDateTime(this.getZonedDateTime().withYear(year));
    }

    public Dater withMonth(int month) {
        return this.zonedDateTime(this.getZonedDateTime().withMonth(month));
    }

    public Dater withDayOfMonth(int dayOfMonth) {
        return this.zonedDateTime(this.getZonedDateTime().withDayOfMonth(dayOfMonth));
    }

    public Dater withDayOfYear(int dayOfYear) {
        return this.zonedDateTime(this.getZonedDateTime().withDayOfYear(dayOfYear));
    }

    public Dater withHour(int hour) {
        return this.zonedDateTime(this.getZonedDateTime().withHour(hour));
    }

    public Dater withMinute(int minute) {
        return this.zonedDateTime(this.getZonedDateTime().withMinute(minute));
    }

    public Dater withSecond(int second) {
        return this.zonedDateTime(this.getZonedDateTime().withSecond(second));
    }

    /**
     * 当前日期开始时间
     *
     * @return Dater
     */
    public Dater start() {
        return this.zonedDateTime(ZonedDateTime.of(this.getLocalDate(), LocalTime.MIN, this.getZoneId()));
    }

    /**
     * 当前日期结束时间
     *
     * @return Dater
     */
    public Dater end() {
        return this.zonedDateTime(ZonedDateTime.of(this.getLocalDate(), LocalTime.MAX, this.getZoneId()));
    }

    /**
     * 当前日期中午时间
     *
     * @return Dater
     */
    public Dater noon() {
        return this.zonedDateTime(ZonedDateTime.of(this.getLocalDate(), LocalTime.NOON, this.getZoneId()));
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
        return this.zonedDateTime(ZonedDateTime.of(this.getLocalDate(), LocalTime.of(hour, 0), this.getZoneId()));
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
                .of(this.offsetDays(dayOffset).getLocalDate(), LocalTime.of(hour - 1, 0, 0).plusHours(1).minusNanos(1L),
                        this.getZoneId()));
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
    public boolean isBefore(Dater dater) {
        return this.getZonedDateTime().isBefore(dater.getZonedDateTime());
    }

    /**
     * 在之后(dater,+∞)
     *
     * @param dater 日期对象
     * @return boolean
     */
    public boolean isAfter(Dater dater) {
        return this.getZonedDateTime().isAfter(dater.getZonedDateTime());
    }

    /**
     * 在之间[startDater, endDater]
     *
     * @param startDater 起始日期
     * @param endDater   结束日期
     * @return boolean
     */
    public boolean isBetween(Dater startDater, Dater endDater) {
        return !startDater.isAfter(endDater) && !this.isAfter(endDater) && !this.isBefore(startDater);
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
     * 年份间隔
     *
     * @param dater 日期对象
     * @return int
     */
    public int getYearsBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getYears());
    }

    /**
     * 月份间隔
     *
     * @param dater 日期对象
     * @return int
     */
    public int getMonthsBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getMonths());
    }

    /**
     * 天数间隔
     *
     * @param dater 日期对象
     * @return int
     */
    public int getDaysBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getDays());
    }

    /**
     * 获取日期+时间
     *
     * @return java.time.LocalDateTime
     */
    public LocalDateTime getLocalDateTime() {
        return this.getZonedDateTime().toLocalDateTime();
    }

    /**
     * 获取日期
     *
     * @return java.time.LocalDate
     */
    public LocalDate getLocalDate() {
        return this.getZonedDateTime().toLocalDate();
    }

    /**
     * 获取时间
     *
     * @return java.time.LocalTime
     */
    public LocalTime getLocalTime() {
        return this.getZonedDateTime().toLocalTime();
    }

    /**
     * 获取日期
     *
     * @return java.util.Date
     */
    public Date getDate() {
        return Date.from(this.getZonedDateTime().toInstant());
    }

    /**
     * 获取时间戳对象
     *
     * @return java.time.Instant
     */
    public Instant getInstant() {
        return this.getZonedDateTime().toInstant();
    }

    /**
     * 获取时间戳（单位：毫秒）
     *
     * @return long
     */
    public long getTimestamp() {
        return this.getZonedDateTime().toInstant().toEpochMilli();
    }

    /**
     * 获取当前时区
     *
     * @return java.time.ZoneId
     */
    public ZoneId getZoneId() {
        return this.getZonedDateTime().getZone();
    }

    /**
     * 获取字符串日期+时间，格式：yyyy-MM-dd HH:mm:ss
     *
     * @return java.lang.String
     */
    public String getDateTimeStr() {
        return this.getFormatterStr(DateUtil.DATETIME_FORMATTER);
    }

    /**
     * 获取字符串日期，格式：yyyy-MM-dd
     *
     * @return java.lang.String
     */
    public String getDateStr() {
        return this.getFormatterStr(DateUtil.DATE_FORMATTER);
    }

    /**
     * 获取字符串时间，格式：HH:mm:ss
     *
     * @return java.lang.String
     */
    public String getTimeStr() {
        return this.getFormatterStr(DateUtil.TIME_FORMATTER);
    }

    /**
     * 获取字符串年月，格式：yyyy-MM
     *
     * @return java.lang.String
     */
    public String getYearMonthStr() {
        return this.getFormatterStr(DateUtil.YEAR_MONTH_FORMATTER);
    }

    /**
     * 获取固定格式日期字符串
     *
     * @param formatter
     * @return java.lang.String
     */
    public String getFormatterStr(DateTimeFormatter formatter) {
        return this.getZonedDateTime().format(formatter);
    }

    public int getYear() {
        return this.getZonedDateTime().getYear();
    }

    public int getMonth() {
        return this.getZonedDateTime().getMonthValue();
    }

    public int getDayOfMonth() {
        return this.getZonedDateTime().getDayOfMonth();
    }

    public int getDayOfYear() {
        return this.getZonedDateTime().getDayOfYear();
    }

    public int getDayOfWeek() {
        return this.getZonedDateTime().getDayOfWeek().getValue();
    }

    public int getHour() {
        return this.getZonedDateTime().getHour();
    }

    public int getMinute() {
        return this.getZonedDateTime().getMinute();
    }

    public int getSecond() {
        return this.getZonedDateTime().getSecond();
    }

    /**
     * 获取月份枚举名称
     *
     * @return java.lang.String
     */
    public String getMonthName() {
        return this.getZonedDateTime().getMonth().name();
    }

    /**
     * 获取星期枚举名称
     *
     * @return java.lang.String
     */
    public String getWeekName() {
        return this.getZonedDateTime().getDayOfWeek().name();
    }

    @Override
    public String toString() {
        return this.getDateTimeStr();
    }

    public static void main(String[] args) {
        System.out.println(Dater.now().getInstant().toEpochMilli());

        System.out.println(Dater.of("2019-12", 7).getDateTimeStr());
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

        System.out.println(Dater.now().getYear());
        System.out.println(Dater.now().getMonth());
        System.out.println(Dater.now().getDayOfYear());
        System.out.println(Dater.now().getDayOfMonth());
        System.out.println(Dater.now().getDayOfWeek());
        System.out.println(Dater.now().getHour());
        System.out.println(Dater.now().getMinute());
        System.out.println(Dater.now().getSecond());

        System.out.println(Dater.now().getMonthName());
        System.out.println(Dater.now().getWeekName());

        System.out.println("between>>>" + Dater.now().getYearsBetween(Dater.of("2021-04-01 00:23:22")));
        System.out.println("between>>>" + Dater.now().getMonthsBetween(Dater.of("2020-03-01 00:23:22")));
        System.out.println("between>>>" + Dater.now().getDaysBetween(Dater.of("2020-03-29 00:23:22")));

        System.out.println(Dater.of("2020-01-01 12:23:24").endOf(24));
        System.out.println(Dater.of("2020-01-01 12:23:24").endOf(0));

        System.out.println(Dater.now(8));
        System.out.println(Dater.now(7));

        Dater ref = Dater.now();
        Dater start = Dater.of(ref.getInstant().toEpochMilli() + "").offsetSeconds(1);
        Dater end = Dater.of(ref.getInstant().toEpochMilli() + "").offsetHours(1);
        System.out.println(ref.getInstant());
        System.out.println(start.getInstant());
        System.out.println(end.getInstant());
        System.out.println(ref.isBetween(start, end));
        System.out.println(ref.isBetween(ref, ref));

        System.out.println(Dater.now().offsetDays(-1).end().getTimestamp());
        System.out.println(Dater.now().endOf(-1).getTimestamp());

        System.out.println(
                Dater.now().withYear(2021).withMonth(2).withDayOfMonth(28).withHour(22).withMinute(33).withSecond(33));

        System.out.println(Dater.now().getDateStr());
        System.out.println(Dater.now().getTimeStr());
        System.out.println(Dater.now().getDateTimeStr());
        System.out.println(Dater.now().getYearMonthStr());
        System.out.println(Dater.of("2020-11-12 15:33"));

    }

}
