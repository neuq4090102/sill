package com.elv.core.tool.application.log.demo;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lxh
 * @since 2021-08-09
 */
public class DemoEnum {

    /**
     * 类型
     */
    public enum TypeEnum {

        FORCE(1, "硬包"), //
        GUARANTEED(2, "软包-有保底"), //
        NO_GUARANTEE(3, "软包-无保底"), //
        ;

        private static Map<Integer, TypeEnum> map;

        static {
            map = Arrays.stream(TypeEnum.values()).collect(Collectors.toMap(key -> key.getValue(), val -> val));
        }

        private final int value;
        private final String desc;

        TypeEnum(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public static TypeEnum itemOf(int value) {
            return map.get(value);
        }
    }

    /**
     * 方式
     */
    public enum GuaranteeWayEnum {

        MONTHLY(1, "每月"), //
        QUARTERLY(2, "每季度"), //
        DURING(3, "合同期内"), //
        ;

        private static Map<Integer, GuaranteeWayEnum> map;

        static {
            map = Arrays.stream(GuaranteeWayEnum.values()).collect(Collectors.toMap(key -> key.getValue(), val -> val));
        }

        private final int value;
        private final String desc;

        GuaranteeWayEnum(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public static GuaranteeWayEnum itemOf(int value) {
            return map.get(value);
        }
    }

    public enum WeekDay {
        SUNDAY(Calendar.SUNDAY, "星期日"), //
        MONDAY(Calendar.MONDAY, "星期一"), //
        TUESDAY(Calendar.TUESDAY, "星期二"), //
        WEDNESDAY(Calendar.WEDNESDAY, "星期三"), //
        THURSDAY(Calendar.THURSDAY, "星期四"), //
        FRIDAY(Calendar.FRIDAY, "星期五"), //
        SATURDAY(Calendar.SATURDAY, "星期六"), //
        ;

        private static Map<Integer, WeekDay> map;

        static {
            map = Arrays.stream(WeekDay.values()).collect(Collectors.toMap(key -> key.getValue(), val -> val));
        }

        private final int value;
        private final String desc;

        WeekDay(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public static WeekDay itemOf(int value) {
            return map.get(value);
        }
    }

}
