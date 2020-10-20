package com.elv.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author lxh
 * @see CollectionUtils
 * @since 2020-09-04
 */
public class CollectionUtil {

    private CollectionUtil() {
    }

    public static boolean isEmpty(Object[] objs) {
        return objs == null || objs.length == 0;
    }

    public static boolean isNotEmpty(Object[] objs) {
        return !isEmpty(objs);
    }

    public static boolean isEmpty(Collection<?> collections) {
        return collections == null || collections.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collections) {
        return !isEmpty(collections);
    }

    public static boolean containAll(Collection<?> collections, Collection<?> childCollections) {
        if (isEmpty(collections) || isEmpty(childCollections)) {
            return false;
        }
        return CollectionUtils.containsAll(collections, childCollections);
    }

    public static boolean containAll(Collection<?> collections, Object... objs) {
        if (isEmpty(collections) || isEmpty(objs)) {
            return false;
        }
        return CollectionUtils.containsAll(collections, Arrays.asList(objs));
    }

    public static boolean containAny(Collection<?> collections, Collection<?> childCollections) {
        if (isEmpty(collections) || isEmpty(childCollections)) {
            return false;
        }
        return CollectionUtils.containsAny(collections, childCollections);
    }

    public static boolean containAny(Collection<?> collections, Object... objs) {
        if (isEmpty(collections) || isEmpty(objs)) {
            return false;
        }
        return CollectionUtils.containsAny(collections, Arrays.asList(objs));
    }

    /**
     * 集合转化为字符串
     *
     * @param collections 集合对象
     * @param delimiter   分隔符
     * @return java.lang.String
     */
    public static <T> String join(Collection<T> collections, CharSequence delimiter) {
        if (isEmpty(collections)) {
            return "";
        }

        for (T collection : collections) {
            if (collection instanceof String) {
                return collections.stream().map(item -> String.valueOf(item)).collect(Collectors.joining(delimiter));
            } else {
                return collections.stream().map(item -> JsonUtil.toJson(item)).collect(Collectors.joining(delimiter));
            }
        }
        return "";
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("aaa", "bbb", "2");
        Set<Long> set = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        Set<String> set2 = new HashSet<>(Arrays.asList("bbb", "ccc"));
        Set<String> set3 = new HashSet<>(Arrays.asList("bbb", "ccc2"));

        System.out.println(join(list, ","));
        System.out.println(join(set, ";"));
        System.out.println(containAny(list, set));
        System.out.println(containAll(list, set2));
        System.out.println(containAll(list, set3));
        System.out.println(containAny(list, set3));
        System.out.println(containAll(list, "ccc", "dd"));
        System.out.println(containAny(list, "ccc", "dd"));
    }
}
