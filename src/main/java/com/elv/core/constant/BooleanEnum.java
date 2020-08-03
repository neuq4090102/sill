package com.elv.core.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lxh
 * @since 2020-03-20
 */
public enum BooleanEnum {

    YES(1), //
    NO(2);

    private static final Map<Integer, BooleanEnum> map;

    static {
        map = Arrays.stream(BooleanEnum.values()).collect(Collectors.toMap(BooleanEnum::getValue, item -> item));
    }

    private final int value; // 枚举的属性字段必须是私有且不可变(无set方法)

    BooleanEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BooleanEnum itemOf(int value) {
        return map.get(value);
    }

    public static boolean isYes(int value) {
        return value == YES.getValue();
    }

    public static boolean isNo(int value) {
        return value == NO.getValue();
    }
}
