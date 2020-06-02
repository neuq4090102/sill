package com.elv.core.util;

import com.elv.traning.model.beanCopy.OrderEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lxh
 * @date 2020-06-01
 */
public class DefaultUtil {


    // TODO
    public static <T> void init(T object) {
        Class<?> clazz = object.getClass();
        List<Field> allFields = BeanUtil.getAllFields(clazz);
        Map<String, Method> getterMap = BeanUtil.getGetterMap(clazz);
        Map<String, Method> setterMap = BeanUtil.getSetterMap(clazz);

        for (Field field : allFields) {
            if (String.class.isAssignableFrom(field.getType())) {
                System.out.println(field.getName() + " is string.");
            } else if (Long.class.isAssignableFrom(field.getType())) {
                System.out.println(field.getName() + " is long.");
            } else if (Collection.class.isAssignableFrom(field.getType())) {
                System.out.println(field.getName() + " is collection.");
            } else {
                System.out.println(field.getName() + " is " + field.getType());
            }
            System.out.println(JsonUtil.toJson(field));

            if (List.class.isAssignableFrom(field.getType())) {
                System.out.println(field.getName() + " is list.");

                System.out.println(JsonUtil.toJson(field));
            }
        }

        // System.out.println(object.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        OrderEntity orderEntity = new OrderEntity();
        init(orderEntity);
    }
}
