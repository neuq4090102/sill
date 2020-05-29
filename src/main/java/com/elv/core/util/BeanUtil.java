package com.elv.core.util;

import com.elv.frame.annotation.desensitization.Blur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lxh
 * @date 2020-04-01
 */
public class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    private BeanUtil() {
    }

    /**
     * spring的copy
     *
     * @param source
     * @param target
     */
    public static void copy(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 单对象转换（不脱敏）
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        return convert(source, targetClass, false);
    }

    /**
     * 单对象转换（支持脱敏）
     *
     * @param source
     * @param targetClass
     * @param needBlur
     * @param <T>
     * @return
     */
    public static <T> T convert(Object source, Class<T> targetClass, boolean needBlur) {
        if (source == null || targetClass == null) {
            return null;
        }

        T result = null;
        try {
            result = targetClass.newInstance();

            Class<?> sourceClass = source.getClass();
            Map<String, Method> getterMap = getterMap(sourceClass);
            Map<String, Method> setterMap = setterMap(targetClass);
            List<Field> allFields = getAllFields(targetClass);
            Map<String, Blur> blurMap = new HashMap<>();
            convert(getterMap, setterMap, allFields, blurMap, source, result, needBlur);
        } catch (Exception e) {
            logger.error("BeanUtil failed to convert.", e);
        }

        return result;
    }

    /**
     * 批量转换（不脱敏）
     *
     * @param sources
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> converts(List<? extends Object> sources, Class<T> targetClass) {
        return converts(sources, targetClass, false);
    }

    /**
     * 批量转换（支持脱敏）
     *
     * @param sources
     * @param targetClass
     * @param needBlur
     * @param <T>
     * @return
     */
    public static <T> List<T> converts(List<?> sources, Class<T> targetClass, boolean needBlur) {
        if (sources == null || sources.size() == 0 || targetClass == null) {
            return Collections.emptyList();
        }

        List<T> results = new ArrayList<>();
        try {
            Class<?> sourceClass = sources.get(0).getClass();
            Map<String, Method> getterMap = getterMap(sourceClass);
            Map<String, Method> setterMap = setterMap(targetClass);
            List<Field> allFields = getAllFields(targetClass);
            Map<String, Blur> blurMap = new HashMap<>();

            for (Object source : sources) {
                T result = targetClass.newInstance();
                results.add(result);

                convert(getterMap, setterMap, allFields, blurMap, source, result, needBlur);
            }
        } catch (Exception e) {
            logger.error("BeanUtil failed to batch convert.", e);
        }

        return results;
    }

    private static <T> void convert(Map<String, Method> getterMap, Map<String, Method> setterMap, List<Field> allFields,
            Map<String, Blur> blurMap, Object source, T result, boolean needBlur)
            throws IllegalAccessException, InvocationTargetException {

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

    private static String desensitize(Object invoke, Blur blur) {
        return Utils.blur(invoke,
                com.elv.frame.model.Blur.builder().fromIdx(blur.fromIdx()).toIdx(blur.toIdx())
                        .stepSize(blur.setpSize()).ratio(blur.ratio()).mask(blur.mask()).build());
    }

    /**
     * 获取类所有属性（含继承属性）
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        return getFields(clazz, true);
    }

    /**
     * 获取类属性
     *
     * @param clazz
     * @param withExtends 是否含继承属性
     * @return
     */
    public static List<Field> getFields(Class<?> clazz, boolean withExtends) {
        List<Field> fields = new ArrayList<>();

        while (withExtends) {
            if (clazz == null || clazz == Object.class) {
                break;
            }
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

            if (!withExtends) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    /**
     * 获取类属性的取值方法
     *
     * @param clazz
     * @return
     * @throws IntrospectionException
     */
    public static Map<String, Method> getterMap(Class<?> clazz) throws IntrospectionException {

        Map<String, Method> resultMap = new HashMap<>();
        if (clazz == null) {
            return resultMap;
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            resultMap.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod());
        }

        return resultMap;
    }

    /**
     * 获取类属性的赋值方法
     *
     * @param clazz
     * @return
     * @throws IntrospectionException
     */
    public static Map<String, Method> setterMap(Class<?> clazz) throws IntrospectionException {

        Map<String, Method> resultMap = new HashMap<>();
        if (clazz == null) {
            return resultMap;
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            resultMap.put(propertyDescriptor.getName(), propertyDescriptor.getWriteMethod());
        }

        return resultMap;
    }
}
