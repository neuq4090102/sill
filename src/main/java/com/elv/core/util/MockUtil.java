package com.elv.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elv.core.model.util.RandomCtrl;
import com.elv.traning.model.beanCopy.OrderEntity;

/**
 * mock and init util - used by myself
 *
 * @author lxh
 * @since 2020-06-01
 */
public class MockUtil {

    private static final Logger logger;
    private static final int UPPER_BOUND_INTEGER;

    public static final String[] LANGUAGES = { "zh-CN", "en", "ko", "jp" };
    public static final String[] NAMES = { "水", "沝", "淼", "㵘", "鱼", "䲆", "鱻", "䲜", "屮", "艸", "芔", "茻", "土", "圭", "垚",
            "㙓", "口", "吅", "品", "㗊", "一", "二", "三", "亖", "又", "㕛", "叒", "叕", "鱼", "䲆", "鱻", "䲜", "火", "炎", "焱", "燚",
            "又", "双", "叒", "叕" };

    static {
        logger = LoggerFactory.getLogger(MockUtil.class);
        UPPER_BOUND_INTEGER = 100;
    }

    /**
     * 模拟生成对象
     *
     * @param beanClass 类对象
     * @param <T>       范型对象
     * @return T
     */
    public static <T> T mock(Class<T> beanClass) {
        T result = null;
        try {
            result = beanClass.newInstance();
            init(result);
        } catch (Exception e) {
            logger.error("MockUtil failed to init.", e);
        }

        return result;
    }

    /**
     * 初始化参数
     *
     * @param object 对象
     * @param <T>    范型对象
     */
    public static <T> void init(T object) {
        Class<?> clazz = object.getClass();
        List<Field> allFields = BeanUtil.getAllFields(clazz);
        Map<String, Method> getterMap = BeanUtil.getGetterMap(clazz);
        Map<String, Method> setterMap = BeanUtil.getSetterMap(clazz);

        try {
            int idx = 0;
            for (Field field : allFields) {
                String fieldName = field.getName();
                Method method = setterMap.get(fieldName);
                if (method == null) {
                    // 无存器，忽略该属性
                    continue;
                }

                Method sourceMethod = getterMap.get(fieldName);
                if (sourceMethod == null) {
                    // 无取器，忽略该属性
                    continue;
                }

                Class<?> fieldType = field.getType();
                Object fieldValue = sourceMethod.invoke(object);
                Object newValue = fieldValue;
                String lowerCaseFieldName = fieldName.toLowerCase();

                field.setAccessible(true);

                if (String.class.isAssignableFrom(fieldType)) {
                    if (fieldValue == null || StrUtil.isEmpty(fieldValue.toString())) {
                        if (lowerCaseFieldName.endsWith("name") || lowerCaseFieldName.endsWith("er")
                                || lowerCaseFieldName.endsWith("or")) {
                            newValue = NAMES[RandomUtil.randomInt(NAMES.length)];
                        } else if (lowerCaseFieldName.endsWith("no")) {
                            newValue = RandomUtil.randomStr(16);
                        } else if (lowerCaseFieldName.endsWith("time")) {
                            newValue = Dater.now().offsetDays(idx).getDateTimeStr();
                        } else if (lowerCaseFieldName.endsWith("date")) {
                            newValue = Dater.now().offsetDays(idx).getDateStr();
                        } else if (lowerCaseFieldName.endsWith("mobile") || lowerCaseFieldName.endsWith("phone")) {
                            newValue = RandomUtil.randomMobile();
                        } else if (lowerCaseFieldName.endsWith("lang")) {
                            newValue = LANGUAGES[RandomUtil.randomInt(LANGUAGES.length)];
                        } else {
                            newValue = fieldName + RandomUtil.randomStr(4);
                        }
                    }
                } else if (Boolean.class.isAssignableFrom(fieldType) || boolean.class.isAssignableFrom(fieldType)) {
                    if (fieldValue == null || (boolean) fieldValue == false) {
                        newValue = RandomUtil.randomStr(10).contains("e");
                    }
                } else if (Double.class.isAssignableFrom(fieldType) || double.class.isAssignableFrom(fieldType)) {
                    if (fieldValue == null || (double) fieldValue == 0.0d) {
                        newValue = RandomUtil.randomDouble();
                    }
                } else if (Float.class.isAssignableFrom(fieldType) || float.class.isAssignableFrom(fieldType)) {
                    if (fieldValue == null || (float) fieldValue == 0.0f) {
                        newValue = RandomUtil.randomFloat();
                    }
                } else if (Long.class.isAssignableFrom(fieldType) || long.class.isAssignableFrom(fieldType)) {
                    if (fieldValue == null || (long) fieldValue == 0L) {
                        newValue = RandomUtil.randomLong();
                        if (lowerCaseFieldName.endsWith("price") || fieldName.contains("money")) {
                            newValue = RandomUtil.randomInt(UPPER_BOUND_INTEGER) * 100L;
                        }
                    }
                } else if (Integer.class.isAssignableFrom(fieldType) || int.class.isAssignableFrom(fieldType)) {
                    if (fieldValue == null || (int) fieldValue == 0) {
                        newValue = RandomUtil.randomInt(UPPER_BOUND_INTEGER);
                    }
                } else if (Short.class.isAssignableFrom(fieldType) || short.class.isAssignableFrom(fieldType)
                        || Byte.class.isAssignableFrom(fieldType) || byte.class.isAssignableFrom(fieldType)) {
                    if (fieldValue == null || (int) fieldValue == 0) {
                        newValue = 1;
                    }
                } else if (Date.class.isAssignableFrom(fieldType)) {
                    if (fieldValue == null) {
                        newValue = Dater.now().getDate();
                    }
                } else if (Collection.class.isAssignableFrom(fieldType)) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        Class<?> rawType = (Class<?>) pt.getRawType();
                        Type actualTypeArgument = pt.getActualTypeArguments()[0];
                        if (actualTypeArgument instanceof ParameterizedType) {
                            // ((ParameterizedType) actualTypeArgument).getActualTypeArguments();
                        } else {
                            Class<?> argument = (Class<?>) actualTypeArgument;
                            Object actualType = newInstance(argument);
                            if (actualType != null && BeanUtil.isBean(argument)) {
                                init(actualType);
                            }

                            Collection<Object> collections = null;
                            if (List.class.isAssignableFrom(rawType)) {
                                collections = new ArrayList<>();
                                collections.add(actualType);
                            } else if (Set.class.isAssignableFrom(rawType)) {
                                collections = new HashSet<>();
                                collections.add(actualType);
                            }
                            newValue = collections;
                        }
                    }
                } else if (Map.class.isAssignableFrom(fieldType)) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;

                        Class<?> rawType = (Class<?>) pt.getRawType();

                        Type keyArgument = pt.getActualTypeArguments()[0];
                        Type valArgument = pt.getActualTypeArguments()[1];
                        if (valArgument instanceof ParameterizedType) {
                            // ((ParameterizedType) actualTypeArgument).getActualTypeArguments();
                        } else {
                            Class<?> keyBeanClass = (Class<?>) keyArgument;
                            Object keyBean = newInstance(keyBeanClass);
                            if (keyBean != null) {
                                if (keyBean != null && BeanUtil.isBean(keyBeanClass)) {
                                    init(keyBean);
                                }

                                Class<?> valBeanClass = (Class<?>) valArgument;
                                Object valBean = newInstance(valBeanClass);
                                if (valBean != null && BeanUtil.isBean(valBeanClass)) {
                                    init(valBean);
                                }

                                Map<Object, Object> maps = null;
                                if (Map.class.isAssignableFrom(rawType)) {
                                    maps = new HashMap<>();
                                    maps.put(keyBean, valBean);
                                }
                                newValue = maps;
                            }
                        }
                    }
                } else {
                    Class<?> beanClass = Class.forName(fieldType.getName());
                    newValue = newInstance(beanClass);
                    if (newValue != null && BeanUtil.isBean(beanClass)) {
                        init(newValue);
                    }
                }

                // System.out.println(fieldName + " is " + field.getType());

                if (newValue != null && !newValue.equals(fieldValue)) {
                    method.invoke(object, newValue);
                }

                idx++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println(object.getClass().getSimpleName());
    }

    /**
     * 创建实例
     *
     * @param clazz 类对象
     * @return java.lang.Object
     */
    private static Object newInstance(Class<?> clazz) {
        if (clazz == null) {
            return null;
        } else if (String.class.isAssignableFrom(clazz)) {
            return RandomUtil.randomStr(RandomCtrl.builder().build());
        }

        Constructor<?> constructor = getSomeOneConstructor(clazz);
        if (constructor == null) {
            return null;
        }

        Object object = null;
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        try {
            if (parameterTypes == null || parameterTypes.length == 0) {
                object = constructor.newInstance();
            } else if (parameterTypes.length == 1) {
                Class<?> parameterType = parameterTypes[0];
                if ("int".equals(parameterType.getName())) {
                    object = constructor.newInstance(RandomUtil.randomInt(UPPER_BOUND_INTEGER));
                } else if ("long".equals(parameterType.getName())) {
                    object = constructor.newInstance(RandomUtil.randomLong());
                } else if ("double".equals(parameterType.getName())) {
                    object = constructor.newInstance(RandomUtil.randomDouble(1000, 2));
                } else {
                    // ignore.
                }
            } else {
                //ignore.
            }
        } catch (Exception e) {
            logger.error("MockUtil failed to getNewInstance.", e);
        }

        return object;
    }

    /**
     * 获取类的某个构造子
     *
     * @param clazz 类对象
     * @return java.lang.reflect.Constructor
     */
    private static Constructor<?> getSomeOneConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors == null || constructors.length == 0) {
            return null;
        }

        Constructor<?> result = constructors[0];
        int min = Integer.MAX_VALUE;
        for (Constructor<?> constructor : constructors) {
            int size = 0;
            if (constructor.getParameterTypes() != null) {
                size = constructor.getParameterTypes().length;
            }

            if (size < min) {
                min = size;
                result = constructor;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderNo("28343");
        MockUtil.init(orderEntity);
        // OrderEntity orderEntity = MockUtil.mock(OrderEntity.class);
        System.out.println(JsonUtil.toJson(orderEntity));

        // MyTest myTest = new MyTest();
        // myTest.getAbc();
        //
        // System.out.println(BeanUtil.isJavaBean(MyTest.class));
        // System.out.println(BeanUtil.isBean(MyTest.class));
    }

    public static class MyTest {

        public MyTest() {
        }

        private String abc;

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }
    }
}



