package com.elv.core.tool.db.redis;

import java.util.Arrays;
import java.util.Set;

import com.elv.core.annotation.redis.Redis;
import com.elv.core.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author lxh
 * @since 2020-06-16
 */

public class RedisCache extends BaseRedis {

    /**
     * @param key     key
     * @param t       储值对象
     * @param seconds 秒数
     * @param <T>     范型
     * @return boolean
     */
    @Redis
    public <T> boolean add(String key, T t, int seconds) {
        String value = t instanceof String ? (String) t : JsonUtil.toJson(t);
        if (!ok(set(key, value))) {
            return false;
        }
        return expire(key, seconds);
    }

    @Redis
    public <T> boolean add(String key, T[] t, int seconds) {
        if (!ok(set(key, JsonUtil.toJson(t)))) {
            return false;
        }
        return expire(key, seconds);
    }

    // TODO add set
    @Redis
    public boolean addSet(String key, String[] values) {
        return success(jedis.sadd(key, values));
    }

    @Redis
    public <T> boolean addSet(String key, T... values) {
        return success(jedis.sadd(key,
                Arrays.stream(values).map(value -> value instanceof String ? (String) value : JsonUtil.toJson(value))
                        .toArray(String[]::new)));
    }

    @Redis
    public Set<String> getSetMembers(String key) {
        return jedis.smembers(key);
    }

    public boolean isSetMember(String key, String value) {
        return jedis.sismember(key, value);
    }

    @Redis
    public boolean remove(String key, String[] values) {
        return success(jedis.srem(key, values));
    }

    @Redis
    public <T> boolean remove(String key, T... values) {
        return success(jedis.srem(key,
                Arrays.stream(values).map(value -> value instanceof String ? (String) value : JsonUtil.toJson(value))
                        .toArray(String[]::new)));
    }

    /**
     * 取值
     *
     * @param key key
     * @return java.lang.String
     */
    @Redis
    public String get(String key) {
        return super.get(key);
    }

    @Redis
    public <T> T get(String key, Class<T> clazz) {
        String result = super.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtil.toObject(result, clazz);
    }

    @Redis
    public <T> T get(String key, TypeReference<T> typeReference) {
        String result = super.get(key);
        if (result == null) {
            return null;
        }
        return JsonUtil.toObject(result, typeReference);
    }

    // TODO  add&get map/list/set

    // public static void family(String key) {
    //     System.out.println("set:" + set(key, "777"));
    //     System.out.println("get:" + get(key));
    //     System.out.println("getSet:" + getSet(key, "888t"));
    //     System.out.println("incr:" + incr(key));
    //     System.out.println("incr:" + incrBy(key, 3));
    //     System.out.println("decr:" + decr(key));
    //     System.out.println("decr:" + decrBy(key, 2));
    //     System.out.println("append:" + append(key, "999"));
    //     System.out.println("length:" + length(key));
    // }

}
