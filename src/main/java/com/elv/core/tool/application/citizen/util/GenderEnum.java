package com.elv.core.tool.application.citizen.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lxh
 * @since 2021-07-01
 */
public enum GenderEnum {
    FEMALE(0, "女"), //
    MALE(1, "男"), //
    SECRET(3, "保密"), //
    ;

    private static final Map<Integer, GenderEnum> map;

    static {
        map = Arrays.stream(GenderEnum.values()).collect(Collectors.toMap(GenderEnum::getValue, item -> item));
    }

    private final int value;
    private final String desc;

    GenderEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static GenderEnum itemOf(int value) {
        return map.get(value);
    }
}
