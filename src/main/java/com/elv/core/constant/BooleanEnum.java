package com.elv.core.constant;

/**
 * @author lxh
 * @date 2020-03-20
 */
public enum BooleanEnum {

    YES(1), NO(2);

    private final int value; // 枚举的属性字段必须是私有且不可变(无set方法)

    private BooleanEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BooleanEnum itemOf(int value) {
        for (BooleanEnum booleanEnum : BooleanEnum.values()) {
            if (booleanEnum.getValue() == value) {
                return booleanEnum;
            }
        }
        return null;
    }

    public static boolean isYes(int value) {
        return value == YES.getValue();
    }

    public static boolean isNo(int value) {
        return value == NO.getValue();
    }

}
