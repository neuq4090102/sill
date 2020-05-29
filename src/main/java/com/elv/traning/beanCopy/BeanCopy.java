package com.elv.traning.beanCopy;

import com.elv.core.util.JsonUtil;
import com.elv.core.util.Utils;
import com.elv.frame.annotation.desensitization.Blur;
import com.elv.traning.model.beanCopy.OrderEntity;
import com.elv.traning.model.beanCopy.OrderResult;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lxh
 * @date 2020-04-09
 */
public class BeanCopy {
    public static void main(String[] args) {
        OrderEntity orderEntity = OrderEntity.builder().orderNo("230343").contacter("张三").mobile("86-13269981234")
                .invoicePrice(20000L).build();
        OrderResult orderResult = convert(orderEntity, OrderResult.class);

        System.out.println(JsonUtil.toJson(orderResult));

        List<OrderEntity> orderEntities = Stream
                .of(OrderEntity.builder().orderNo("2303").contacter("张三").mobile("86-13824321234").invoicePrice(20000L)
                                .build(), OrderEntity.builder().orderNo("1331").contacter("李四").mobile("86-18343344008")
                                .invoicePrice(30000L).build(),
                        OrderEntity.builder().orderNo("1342").contacter("王五").mobile("86-17089192234")
                                .invoicePrice(40000L).build()).collect(Collectors.toList());

        List<OrderResult> orderResults = convert(orderEntities, OrderResult.class, true);

        System.out.println(JsonUtil.toJson(orderResults));

    }

    private static <T> List<T> convert(List<?> sources, Class<T> targetClass, boolean needBlur) {
        if (sources == null || sources.size() == 0 || targetClass == null) {
            return Collections.emptyList();
        }

        List<T> results = new ArrayList<>();
        try {
            Class<?> sourceClass = sources.get(0).getClass();
            Map<String, Method> getterMap = BeanReflect.getterMap(sourceClass);
            Map<String, Method> setterMap = BeanReflect.setterMap(targetClass);
            List<Field> allFields = getAllFields(targetClass);
            Map<String, Blur> blurMap = new HashMap<>();

            for (Object source : sources) {
                T result = targetClass.newInstance();
                results.add(result);

                for (Field targetField : allFields) {
                    if (Modifier.isStatic(targetField.getModifiers()) || Modifier.isFinal(targetField.getModifiers())) {
                        continue;
                    }

                    Method sourceMethod = getterMap.get(targetField.getName());
                    if (sourceMethod == null) {
                        continue;
                    }

                    Object sourceGetValue = sourceMethod.invoke(source);
                    if (sourceGetValue == null) {
                        continue;
                    }

                    // 需要脱敏且可以脱敏
                    if (needBlur && String.class.isAssignableFrom(targetField.getType())) {
                        Blur blur = blurMap.get(targetField.getName());
                        if (blur == null) {
                            blur = targetField.getAnnotation(Blur.class);
                            if (blur != null) {
                                blurMap.put(targetField.getName(), blur);
                            }
                        }

                        if (blur != null) {
                            targetField.setAccessible(true);
                            sourceGetValue = desensitize(sourceGetValue, blur);
                        }
                    }

                    // targetField.set(result, sourceGetValue); //此种方式，如果没有set方法将报错
                    Method method = setterMap.get(targetField.getName());
                    if (method != null) {
                        method.invoke(result, sourceGetValue);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    private static <T> T convert(Object source, Class<T> targetClass) {
        if (!canCopy(source, targetClass)) {
            return null;
        }

        Class<?> sourceClass = source.getClass();

        T result = null;
        try {
            result = targetClass.newInstance();

            Map<String, Method> getterMap = BeanReflect.getterMap(sourceClass);
            Map<String, Method> setterMap = BeanReflect.setterMap(targetClass);
            for (Field targetField : getAllFields(targetClass)) {
                if (Modifier.isStatic(targetField.getModifiers()) || Modifier.isFinal(targetField.getModifiers())) {
                    continue;
                }

                Method sourceMethod = getterMap.get(targetField.getName());
                if (sourceMethod == null) {
                    continue;
                }

                Object invoke = sourceMethod.invoke(source);
                if (invoke == null) {
                    continue;
                }

                if (String.class.isAssignableFrom(targetField.getType())) {
                    Blur blur = targetField.getAnnotation(Blur.class);
                    if (blur != null) {
                        targetField.setAccessible(true);
                        invoke = desensitize(invoke, blur);
                    }
                }
                // targetField.set(result, invoke); //此种方式，如果没有set方法将报错
                Method method = setterMap.get(targetField.getName());
                if (method != null) {
                    method.invoke(result, invoke);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String desensitize(Object invoke, Blur blur) {
        return Utils.blur(invoke,
                com.elv.frame.model.Blur.builder().fromIdx(blur.fromIdx()).toIdx(blur.toIdx()).stepSize(blur.setpSize())
                        .ratio(blur.ratio()).mask(blur.mask()).build());
    }

    private static void copy(Object source, Object target) {
        if (!canCopy(source, target)) {
            return;
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        try {
            Map<String, Method> getterMap = BeanReflect.getterMap(sourceClass);
            Map<String, Method> setterMap = BeanReflect.setterMap(targetClass);
            for (Field targetField : getAllFields(targetClass)) {
                System.out.println(Modifier.toString(targetField.getModifiers()) + " " + targetField.getName());
                if (Modifier.isStatic(targetField.getModifiers()) || Modifier.isFinal(targetField.getModifiers())) {
                    continue;
                }

                Method sourceMethod = getterMap.get(targetField.getName());
                if (sourceMethod == null) {
                    continue;
                }
                Object invoke = sourceMethod.invoke(source);
                Method method = setterMap.get(targetField.getName());
                method.invoke(target, invoke);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        while (true) {
            if (clazz == null || clazz == Object.class) {
                break;
            }
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    private static boolean canCopy(Object source, Object target) {
        if (source == null || target == null) {
            return false;
        }

        return true;
    }
}
