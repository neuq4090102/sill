package com.elv.core.util;

import java.beans.BeanInfo;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.elv.core.annotation.desensitization.Blur;
import com.elv.core.model.util.BlurCtrl;

/**
 * @author lxh
 * @since 2020-04-01
 */
public class BeanUtil {

    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(BeanUtil.class);
    }

    private BeanUtil() {
    }

    /**
     * 获取类所有属性（含继承属性）
     *
     * @param beanClass 类对象
     * @return java.util.List
     */
    public static List<Field> getAllFields(Class<?> beanClass) {
        return getFields(beanClass, true);
    }

    /**
     * 获取类属性
     *
     * @param beanClass   类对象
     * @param withExtends 是否含继承属性
     * @return java.util.List
     */
    public static List<Field> getFields(Class<?> beanClass, boolean withExtends) {
        List<Field> fields = new ArrayList<>();

        while (withExtends) {
            if (beanClass == null || beanClass == Object.class) {
                break;
            }
            fields.addAll(Arrays.asList(beanClass.getDeclaredFields()));

            if (!withExtends) {
                break;
            }
            beanClass = beanClass.getSuperclass();
        }

        return fields;
    }

    /**
     * 获取类属性的取值方法
     *
     * @param beanClass 类对象
     * @return java.util.Map
     */
    public static Map<String, Method> getGetterMap(Class<?> beanClass) {

        Map<String, Method> resultMap = new HashMap<>();
        if (beanClass == null) {
            return resultMap;
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);

            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                resultMap.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod());
            }
        } catch (Throwable t) {
            // 不能解析类信息
            throw new RuntimeException("BeanUtil can't parse class info:+" + beanClass.getName());
        }

        return resultMap;
    }

    /**
     * 获取类属性的赋值方法
     *
     * @param beanClass 类对象
     * @return java.util.Map
     */
    public static Map<String, Method> getSetterMap(Class<?> beanClass) {

        Map<String, Method> resultMap = new HashMap<>();
        if (beanClass == null) {
            return resultMap;
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                resultMap.put(propertyDescriptor.getName(), propertyDescriptor.getWriteMethod());
            }
        } catch (Throwable t) {
            // 不能解析类信息
            throw new RuntimeException("BeanUtil can't parse class info:+" + beanClass.getName());
        }

        return resultMap;
    }

    /**
     * spring的copy
     *
     * @param source 源对象
     * @param target 目标对象
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
     * @param source      源对象
     * @param targetClass 目标类对象
     * @param <T>         范型
     * @return T
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        return convert(source, targetClass, false);
    }

    /**
     * 单对象转换（支持脱敏）
     *
     * @param source      源对象
     * @param targetClass 目标类对象
     * @param needBlur    是否需要脱敏
     * @param <T>         范型
     * @return T
     */
    public static <T> T convert(Object source, Class<T> targetClass, boolean needBlur) {
        if (source == null || targetClass == null) {
            return null;
        }

        T result = null;
        try {
            result = targetClass.newInstance();

            Class<?> sourceClass = source.getClass();
            Map<String, Method> getterMap = getGetterMap(sourceClass);
            Map<String, Method> setterMap = getSetterMap(targetClass);
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
     * @param sources     源对象
     * @param targetClass 目标类对象
     * @param <T>         范型
     * @return java.util.List
     */
    public static <T> List<T> converts(List<? extends Object> sources, Class<T> targetClass) {
        return converts(sources, targetClass, false);
    }

    /**
     * 批量转换（支持脱敏）
     *
     * @param sources     源对象
     * @param targetClass 目标类对象
     * @param needBlur    是否需要脱敏
     * @param <T>         范型
     * @return java.util.List
     */
    public static <T> List<T> converts(List<?> sources, Class<T> targetClass, boolean needBlur) {
        if (sources == null || sources.size() == 0 || targetClass == null) {
            return Collections.emptyList();
        }

        List<T> results = new ArrayList<>();
        try {
            Class<?> sourceClass = sources.get(0).getClass();
            Map<String, Method> getterMap = getGetterMap(sourceClass);
            Map<String, Method> setterMap = getSetterMap(targetClass);
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

    /**
     * 脱敏
     *
     * @param object 脱敏对象
     * @param blur   脱敏参数
     * @return java.lang.String
     */
    private static String desensitize(Object object, Blur blur) {
        return StrUtil.blur(object,
                BlurCtrl.builder().fromIdx(blur.fromIdx()).toIdx(blur.toIdx()).stepSize(blur.setpSize())
                        .ratio(blur.ratio()).mask(blur.mask()).build());
    }

}
