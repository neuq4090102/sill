package com.elv.core.tool.db.redis;

import javax.annotation.Resource;

/**
 * @author lxh
 * @since 2020-08-26
 */
public abstract class AbstractCache {

    @Resource
    protected RedisCache redisCache;

    protected abstract String key();

    protected abstract <T> void add(T t);

    protected abstract <T> T get();

    protected int ttl() {
        return 24 * 3600; // 单位：秒
    }

    protected <T> void defaultAdd(T t) {
        redisCache.add(key(), t, ttl());
    }

    protected String defaultGet() {
        return redisCache.get(key());
    }

    protected <T> T defaultGet(Class<T> clazz) {
        return redisCache.get(key(), clazz);
    }

}
