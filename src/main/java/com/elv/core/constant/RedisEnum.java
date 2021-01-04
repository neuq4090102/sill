package com.elv.core.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lxh
 * @since 2020-08-26
 */
public class RedisEnum {

    public enum DataTypeEnum {
        NONE("none"), // key不存在
        STRING("string"), // 字符串
        LIST("list"), // 列表
        SET("set"), // 集合
        SORTED_SET("zset"), // 有序集
        HASH("hash"), // 哈希表
        ;

        private static final Map<String, DataTypeEnum> map;

        static {
            map = Arrays.stream(DataTypeEnum.values()).collect(Collectors.toMap(key -> key.getType(), val -> val));
        }

        private final String type;

        DataTypeEnum(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static DataTypeEnum itemOf(String type) {
            return map.get(type);
        }

        public static boolean isValid(String type) {
            return itemOf(type) != NONE;
        }

        public static boolean isInvalid(String type) {
            return itemOf(type) == NONE;
        }

        public static boolean isString(String type) {
            return itemOf(type) == STRING;
        }

        public static boolean isList(String type) {
            return itemOf(type) == LIST;
        }

        public static boolean isNotList(String type) {
            return itemOf(type) != LIST;
        }

        public static boolean isSet(String type) {
            return itemOf(type) == SET;
        }

        public static boolean isSortedSet(String type) {
            return itemOf(type) == SORTED_SET;
        }

        public static boolean isHash(String type) {
            return itemOf(type) == HASH;
        }
    }

    public enum LockTypeEnum {
        XX("xx"), //
        NX("nx"), //
        PX("px"), //
        EX("ex"), //
        ;

        private final String type;

        LockTypeEnum(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
