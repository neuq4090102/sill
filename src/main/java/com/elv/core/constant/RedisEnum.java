package com.elv.core.constant;

/**
 * desc
 *
 * @author lxh
 * @since 2020-08-26
 */
public class RedisEnum {

    public enum TypeEnum {
        NONE("none"), // key不存在
        STRING("string"), // 字符串
        LIST("list"), // 列表
        SET("set"), // 集合
        ZSET("zset"), // 有序集
        HASH("hash"), // 哈希表
        ;

        private final String type;

        TypeEnum(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static boolean is(String type, TypeEnum typeEnum) {
            return typeEnum.getType().equals(type);
        }
    }
}
