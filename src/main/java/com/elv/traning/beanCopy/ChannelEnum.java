package com.elv.traning.beanCopy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.elv.core.util.BeanUtil;
import com.elv.core.util.JsonUtil;

/**
 * @author lxh
 * @date 2020-06-05
 */
public enum ChannelEnum {

    BASIC("basic"), //
    TUJIA("tujia"),  //
    ZHENGUO("zhenguo"), //
    ;

    private static Map<String, ChannelEnum> map = new HashMap<>();

    static {
        for (ChannelEnum item : ChannelEnum.values()) {
            map.put(item.getCode(), item);
        }
    }

    ChannelEnum(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }



    public static void main(String[] args) {
        Class<?> enumClass = ChannelEnum.class;

        System.out.println(Enum.class.isAssignableFrom(enumClass));

        testOne(enumClass);
    }

    private static void testOne(Class<?> enumClass) {
        Method method = BeanUtil.getGetterMap(enumClass).get("code");
        if (method != null) {
            System.out.println(JsonUtil.toJson(method));
        }

        try {
            for (Object item : enumClass.getEnumConstants()) {
                System.out.println(JsonUtil.toJson(item));
                Object invoke = method.invoke(item);
                System.out.println(">>>" + invoke);
            }
        } catch (Exception e) {

        }
    }

}
