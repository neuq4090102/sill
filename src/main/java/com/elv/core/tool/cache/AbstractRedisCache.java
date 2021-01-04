package com.elv.core.tool.cache;

import java.util.Optional;

import javax.annotation.Resource;

import com.elv.core.tool.db.redis.RedisCache;

/**
 * @author lxh
 * @since 2020-08-26
 */
public abstract class AbstractRedisCache {

    @Resource
    protected RedisCache redisCache;

    protected <T> void add(String key, T t) {
        redisCache.add(this.keyOf(key), t, ttl());
    }

    protected String get(String key) {
        return redisCache.get(this.keyOf(key));
    }

    protected <T> T get(String key, Class<T> clazz) {
        return redisCache.get(this.keyOf(key), clazz);
    }

    protected abstract String getKeyPrefix();

    protected String getKeySuffix() {
        return "";
    }

    protected int ttl() {
        return 24 * 3600; // 单位：秒
    }

    protected String keyOf(String key) {
        return Optional.ofNullable(getKeyPrefix()).orElse("") + key + Optional.ofNullable(getKeySuffix()).orElse("");
    }

}
