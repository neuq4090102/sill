package com.elv.core.tool.db.redis;

/**
 * @author lxh
 * @since 2020-08-26
 */
public abstract class AbstractCache {

    protected int ttl = 24 * 3600; // 单位：秒
    protected RedisCache redisCache;

    protected abstract String key();

}
