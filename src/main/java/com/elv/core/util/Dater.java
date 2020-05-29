package com.elv.core.util;

import com.elv.sill.framework.constant.Const;
import org.apache.commons.lang3.StringUtils;

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

/**
 * Date & Time util
 *
 * @author lxh
 * @date 2020-03-23
 */
public class Dater {

    private ZonedDateTime zonedDateTime;

    private Dater() { //工具类中尽量屏蔽构造方法（私有化）
        zonedDateTime = ZonedDateTime.now();
    }

    public static Dater of() {
        return new Dater();
    }

    public static Dater of(int timeZone) {
        ZoneId zoneId = getZoneId(timeZone);
        Dater dater = new Dater();
        dater.setZonedDateTime(ZonedDateTime.of(LocalDateTime.now(zoneId), zoneId));
        return dater;
    }

    public static Dater of(String dateStr) {
        return of(dateStr, Const.DEFAULT_TIME_ZONE);
    }

    public static Dater of(String dateStr, int timeZone) {
        return of(dateStr, getZoneId(timeZone));
    }

    private static Dater of(String dateStr, ZoneId zoneId) {
        Dater dater = new Dater();
        if (StringUtils.isBlank(dateStr) || zoneId == null) {
            return dater;
        }

        try {
            if (dateStr.length() == 10 && Utils.isNum(dateStr)) {
                dater.setZonedDateTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(dateStr)), zoneId));
            } else if (dateStr.length() == 13 && Utils.isNum(dateStr)) {
                dater.setZonedDateTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(dateStr)), zoneId));
            } else if (dateStr.length() == 7 && DateUtil.isYearMonth(dateStr)) {
                String[] dateStrArr = dateStr.split("-");
                LocalDate localDate = LocalDate.of(Integer.parseInt(dateStrArr[0]), Integer.parseInt(dateStrArr[1]), 1);
                dater.setZonedDateTime(ZonedDateTime.of(localDate, LocalTime.MIN, zoneId));
            } else if (dateStr.length() == 10 && DateUtil.isDate(dateStr)) {
                String[] dateStrArr = dateStr.split("-");
                LocalDate localDate = LocalDate.of(Integer.parseInt(dateStrArr[0]), Integer.parseInt(dateStrArr[1]),
                        Integer.parseInt(dateStrArr[2]));
                dater.setZonedDateTime(ZonedDateTime.of(localDate, LocalTime.MIN, zoneId));
            } else {
                LocalDateTime localDateTime = LocalDateTime
                        .parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                dater.setZonedDateTime(ZonedDateTime.of(localDateTime, zoneId));
            }
        } catch (Throwable t) {
            throw new RuntimeException("日期参数无法识别：" + dateStr);
        }

        return dater;
    }

    /**
     * 获取时区对象
     * <p>timeZone的取值范围：[-12, 12],其中-12和12表示同一时区</p>
     *
     * @param timeZone
     * @return ZoneId
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

    //    *************************************************************************

    public static Dater now() {
        return new Dater();
    }

    /**
     * 当前日期开始时间
     *
     * @return Dater
     */
    public Dater start() {
        this.setZonedDateTime(ZonedDateTime.of(this.getLocalDate(), LocalTime.MIN, this.getZoneId()));
        return this;
    }

    /**
     * 当前日期结束时间
     *
     * @return Dater
     */
    public Dater end() {
        this.setZonedDateTime(ZonedDateTime.of(this.getLocalDate(), LocalTime.MAX, this.getZoneId()));
        return this;
    }

    /**
     * 当前日期中午时间
     *
     * @return Dater
     */
    public Dater noon() {
        this.setZonedDateTime(ZonedDateTime.of(this.getLocalDate(), LocalTime.NOON, this.getZoneId()));
        return this;
    }

    /**
     * 当前日期以小时数为hour开始的时间点
     * <pre>
     *  Dater.of("2020-01-01 12:23:24").startOf(4)  >>  2020-01-01 04:00:00
     * </pre>
     *
     * @param hour
     * @return Dater
     */
    public Dater startOf(int hour) {
        if (hour < 0 || hour > 23) {
            hour = 0;
        }
        this.setZonedDateTime(ZonedDateTime.of(this.getLocalDate(), LocalTime.of(hour, 0), this.getZoneId()));
        return this;
    }

    /**
     * 当前日期以小时数为hour结尾的时间点
     * <pre>
     *  Dater.of("2020-01-01 12:23:24").endOf(24)  >>  2020-01-01 23:59:59
     *  Dater.of("2020-01-01 12:23:24").endOf(0)   >>  2019-12-31 23:59:59
     * </pre>
     *
     * @param hour
     * @return Dater
     */
    public Dater endOf(int hour) {
        int dayOffset = 0;
        if (hour <= 0) {
            hour = 24;
            dayOffset = -1;
        } else if (hour > 23) {
            hour = 24;
        }
        this.setZonedDateTime(ZonedDateTime
                .of(this.offsetDays(dayOffset).getLocalDate(), LocalTime.of(hour - 1, 59, 59), this.getZoneId()));
        return this;
    }

    /**
     * 当前日期按年数偏移
     *
     * @param years
     * @return Dater
     */
    public Dater offsetYears(long years) {
        this.setZonedDateTime(this.getZonedDateTime().plusYears(years));
        return this;
    }

    /**
     * 当前日期按月份数偏移
     *
     * @param months
     * @return Dater
     */
    public Dater offsetMonths(long months) {
        this.setZonedDateTime(this.getZonedDateTime().plusMonths(months));
        return this;
    }

    /**
     * 当前日期按天数偏移
     *
     * @param days
     * @return Dater
     */
    public Dater offsetDays(long days) {
        this.setZonedDateTime(this.getZonedDateTime().plusDays(days));
        return this;
    }

    /**
     * 当前日期按星期数偏移
     *
     * @param weeks
     * @return Dater
     */
    public Dater offsetWeeks(long weeks) {
        this.setZonedDateTime(this.getZonedDateTime().plusWeeks(weeks));
        return this;
    }

    /**
     * 当前日期按小时数偏移
     *
     * @param hours
     * @return Dater
     */
    public Dater offsetHours(long hours) {
        this.setZonedDateTime(this.getZonedDateTime().plusHours(hours));
        return this;
    }

    /**
     * 当前日期按分钟数偏移
     *
     * @param minutes
     * @return Dater
     */
    public Dater offsetMinutes(long minutes) {
        this.setZonedDateTime(this.getZonedDateTime().plusMinutes(minutes));
        return this;
    }

    /**
     * 当前日期按秒数偏移
     *
     * @param seconds
     * @return Dater
     */
    public Dater offsetSeconds(long seconds) {
        this.setZonedDateTime(this.getZonedDateTime().plusSeconds(seconds));
        return this;
    }

    /**
     * 当前日期所在年初
     *
     * @return Dater
     */
    public Dater firstDayOfYear() {
        this.setZonedDateTime(this.getZonedDateTime().with(TemporalAdjusters.firstDayOfYear()));
        return this;
    }

    /**
     * 当前日期所在年末
     *
     * @return Dater
     */
    public Dater lastDayOfYear() {
        this.setZonedDateTime(this.getZonedDateTime().with(TemporalAdjusters.lastDayOfYear()));
        return this;
    }

    /**
     * 当前日期所在月初
     *
     * @return Dater
     */
    public Dater firstDayOfMonth() {
        this.setZonedDateTime(this.getZonedDateTime().with(TemporalAdjusters.firstDayOfMonth()));
        return this;
    }

    /**
     * 当前日期所在月末
     *
     * @return Dater
     */
    public Dater lastDayOfMonth() {
        this.setZonedDateTime(this.getZonedDateTime().with(TemporalAdjusters.lastDayOfMonth()));
        return this;
    }

    /**
     * 是否在dater之前
     *
     * @param dater
     * @return boolean
     */
    public boolean isBefore(Dater dater) {
        return this.getZonedDateTime().isBefore(dater.getZonedDateTime());
    }

    /**
     * 是否在dater之后
     *
     * @param dater
     * @return boolean
     */
    public boolean isAfter(Dater dater) {
        return this.getZonedDateTime().isAfter(dater.getZonedDateTime());
    }

    /**
     * 年份间隔
     *
     * @param dater
     * @return int
     */
    public int getYearsBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getYears());
    }

    /**
     * 月份间隔
     *
     * @param dater
     * @return int
     */
    public int getMonthsBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getMonths());
    }

    /**
     * 天数间隔
     *
     * @param dater
     * @return int
     */
    public int getDaysBetween(Dater dater) {
        return Math.abs(Period.between(this.getZonedDateTime().toLocalDate(), dater.getZonedDateTime().toLocalDate())
                .getDays());
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
     * @return LocalDateTime
     */
    private LocalDateTime getLocalDateTime() {
        return this.getZonedDateTime().toLocalDateTime();
    }

    /**
     * 获取日期
     *
     * @return LocalDate
     */
    private LocalDate getLocalDate() {
        return this.getZonedDateTime().toLocalDate();
    }

    /**
     * 获取时间
     *
     * @return LocalTime
     */
    private LocalTime getLocalTime() {
        return this.getZonedDateTime().toLocalTime();
    }

    /**
     * 获取日期
     *
     * @return Date
     */
    public Date getDate() {
        return Date.from(this.getZonedDateTime().toInstant());
    }

    /**
     * 获取时间戳
     *
     * @return Instant
     */
    public Instant getInstant() {
        return this.getZonedDateTime().toInstant();
    }

    /**
     * 获取当前时区
     *
     * @return ZoneId
     */
    public ZoneId getZoneId() {
        return this.getZonedDateTime().getZone();
    }

    /**
     * 获取字符串日期+时间
     *
     * @return String，格式：yyyy-MM-dd HH:mm:ss
     */
    public String getDateTimeStr() {
        return this.getZonedDateTime().format(DateTimeFormatter.ofPattern(DateUtil.DATE_TIME));
    }

    /**
     * 获取字符串日期
     *
     * @return String，格式：yyyy-MM-dd
     */
    public String getDateStr() {
        return this.getZonedDateTime().format(DateTimeFormatter.ofPattern(DateUtil.DATE));
    }

    /**
     * 获取字符串年月
     *
     * @return String，格式：yyyy-MM
     */
    public String getYearMonthStr() {
        return this.getZonedDateTime().format(DateTimeFormatter.ofPattern(DateUtil.DATE_YM));
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
     * @return String
     */
    public String getMonthName() {
        return this.getZonedDateTime().getMonth().name();
    }

    /**
     * 获取星期枚举名称
     *
     * @return String
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

    }

}
