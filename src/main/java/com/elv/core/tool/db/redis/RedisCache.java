package com.elv.core.tool.db.redis;

import java.util.Arrays;
import java.util.Map;
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
     * 储值
     *
     * @param key key
     * @param t   值
     * @param <T> 范型
     * @return java.lang.String
     */
    @Redis
    public <T> boolean add(String key, T t) {
        return success(jedis.set(key, t instanceof String ? (String) t : JsonUtil.toJson(t)));
    }

    /**
     * @param key     key
     * @param t       储值对象
     * @param seconds 秒数
     * @param <T>     范型
     * @return boolean
     */
    @Redis
    public <T> boolean add(String key, T t, int seconds) {
        if (!success(jedis.set(key, t instanceof String ? (String) t : JsonUtil.toJson(t)))) {
            return false;
        }
        return expire(key, seconds);
    }

    @Redis
    public <T> boolean add(String key, T[] t, int seconds) {
        if (!success(jedis.set(key, JsonUtil.toJson(t)))) {
            return false;
        }
        return expire(key, seconds);
    }

    /**
     * 取值
     *
     * @param key key
     * @return java.lang.String
     */
    @Redis
    public String get(String key) {
        return jedis.get(key);
    }

    @Redis
    public <T> T get(String key, Class<T> clazz) {
        return JsonUtil.toObject(jedis.get(key), clazz);
    }

    @Redis
    public <T> T get(String key, TypeReference<T> reference) {
        return JsonUtil.toObject(jedis.get(key), reference);
    }

    /*********************************** set begin ***********************************/
    /**
     * @param key    key
     * @param values 储值对象
     * @return boolean
     */
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
    public boolean addSet(String key, String[] values, int seconds) {
        if (!success(jedis.sadd(key, values))) {
            return false;
        }
        return expire(key, seconds);
    }

    @Redis
    public Set<String> membersOf(String key) {
        return jedis.smembers(key);
    }

    @Redis
    public boolean isMember(String key, String value) {
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

    /*********************************** set end ***********************************/
    /*********************************** sorted set begin ***********************************/
    @Redis
    public boolean addSortedSet(String key, Map<String, Double> members) {
        return success(jedis.zadd(key, members));
    }

    @Redis
    public boolean addSortedSet(String key, Map<String, Double> members, int seconds) {
        if (!success(jedis.zadd(key, members))) {
            return false;
        }

        return expire(key, seconds);
    }

    @Redis
    public Set<String> rangeOf(String key, long start, long stop) {
        return jedis.zrange(key, start, stop);
    }

    /*********************************** sorted set end ***********************************/

}
