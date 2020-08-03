package com.elv.traning.beanCopy;

import com.elv.traning.model.beanReflect.Country;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lxh
 * @since 2020-04-09
 */
public class BeanReflect {

    public static void main(String[] args) {
        Class<?> clazz = Country.class;

        //        testClass(clazz);
        //        testAnnotation(clazz);
        //        testConstructor(clazz);
        testField(clazz);
        //        testMethod(clazz);
        //        testOther(clazz);

        //        try {
        //            Map<String, Method> getterMap = getterMap(clazz);
        //            for (Map.Entry<String, Method> entry : getterMap.entrySet()) {
        //                System.out.println(entry.getKey() + "," + entry.getValue().getName());
        //            }
        //        } catch (IntrospectionException e) {
        //            e.printStackTrace();
        //        }

    }

    private static void testOther(Class<?> clazz) {
        System.out.println(clazz.isEnum()); //枚举
        System.out.println(clazz.isArray()); //数组
        System.out.println(clazz.isInterface()); //接口
        System.out.println(clazz.isAnnotation()); //注解类
        System.out.println(clazz.isLocalClass()); //局部类
        System.out.println(clazz.isMemberClass()); //内部类
    }

    private static void testMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods(); //所有public方法，含继承的方法
        ObjectMapper mapper = new ObjectMapper();

        for (Method method : methods) {
            System.out.println("from pubic:" + method.getName() + ", paramCount>" + method.getParameterCount());
        }

        Method[] declaredMethods = clazz.getDeclaredMethods();  //所有改类的方法
        for (Method declaredMethod : declaredMethods) {
            //            System.out.println(
            //                    "from all:" + declaredMethod.getName() + ", paramCount>" + declaredMethod.getParameterCount());

            //            System.out.println(declaredMethod.getReturnType().toString().contains("void"));
            try {
                System.out.println(mapper.writeValueAsString(declaredMethod));
            } catch (JsonProcessingException e) {
                //                e.printStackTrace();
            }
        }
    }

    private static void testField(Class<?> clazz) {
        Field[] fields = clazz.getFields(); //所有public属性,含继承
        for (Field field : fields) {
            System.out.println("from pubic:" + field.getName() + ", type>" + field.getType() + ",from:" + field
                    .getDeclaringClass());
        }

        Field[] declaredFields = clazz.getDeclaredFields(); //类所有属性
        for (Field declaredField : declaredFields) {
            System.out.println("from all:" + declaredField.getName() + ", type>" + declaredField.getType());
        }

        //属性去重
        //        List<Field> allFields = Stream.of(Arrays.stream(fields), Arrays.stream(declaredFields))
        //                .flatMap(Function.identity()).distinct().collect(Collectors.toList());

        List<Field> allFields = getAllFields(clazz);
        for (Field allField : allFields) {
            // Modifier:修饰符工具类，
            //            System.out.println(Modifier.isFinal(allField.getModifiers()));
            System.out.println(
                    "from stream:" + allField.getName() + ", type>" + allField.getType() + ", modifiers=" + allField
                            .getModifiers());
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

    private static void testConstructor(Class<?> clazz) {
    }

    private static void testAnnotation(Class<?> clazz) {
    }

    private static void testClass(Class<?> clazz) {

        System.out.println(clazz.getSimpleName());
        System.out.println(clazz.getName());
        System.out.println(clazz.getPackage());
        Class<?> superclass = clazz.getSuperclass();
        System.out.println(superclass.getName());

        Class<?>[] interfaces = clazz.getInterfaces();
        System.out.println(Arrays.stream(interfaces).peek(itf -> System.out.println(itf.getName())).count());

    }

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
