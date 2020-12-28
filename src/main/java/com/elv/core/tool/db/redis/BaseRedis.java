package com.elv.core.tool.db.redis;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.elv.core.constant.RedisEnum.TypeEnum;

import redis.clients.jedis.Jedis;

/**
 * @author lxh
 * @since 2020-08-25
 */
public abstract class BaseRedis {

    public static final long FOREVER_EXIST = -1L;
    public static final long NOT_EXIST = -2L;
    public static final String OK = "OK";
    public static final long SUCCESS = 1L;

    private RedisAccess redisAccess;
    protected Jedis jedis;

    /**
     * key是否存在
     *
     * @param key key
     * @return boolean
     */
    public boolean exists(String key) {
        return Optional.ofNullable(jedis.exists(key)).orElse(false);
    }

    /**
     * 储值类型
     * 不存在则返回"none"
     *
     * @param key key
     * @return java.lang.String
     */
    public String type(String key) {
        return jedis.type(key);
    }

    /**
     * 以秒为单位，返回key的剩余生存时间
     * 当key不存在时，返回-2，当key存在但没有设置剩余生存时间时，返回-1
     *
     * @param key key
     * @return long
     */
    public long ttl(String key) {
        return Optional.ofNullable(jedis.ttl(key)).orElse(NOT_EXIST);
    }

    /**
     * 以毫秒为单位，返回key的剩余生存时间
     * 当key不存在时，返回-2，当key存在但没有设置剩余生存时间时，返回-1
     *
     * @param key key
     * @return long
     */
    public long pttl(String key) {
        return Optional.ofNullable(jedis.pttl(key)).orElse(NOT_EXIST);
    }

    /**
     * 重命名key
     *
     * @param oldKey 旧key
     * @param newKey 新key
     * @return boolean
     */
    public boolean rename(String oldKey, String newKey) {
        if (!exists(oldKey)) {
            return false;
        }
        return success(jedis.rename(oldKey, newKey));
    }

    /**
     * 以秒为单位给key设置过期时间
     * 设置成功返回1，当key不存在或者设置失败则返回0
     *
     * @param key     key
     * @param seconds 秒数
     * @return boolean
     */
    public boolean expire(String key, int seconds) {
        if (seconds <= 0) {
            return false;
        }
        return success(jedis.expire(key, seconds));
    }

    /**
     * 以毫秒为单位给key设置过期时间
     * 设置成功返回1，当key不存在或者设置失败则返回0
     *
     * @param key          key
     * @param milliseconds 毫秒数
     * @return boolean
     */
    public boolean pexpire(String key, long milliseconds) {
        if (milliseconds <= 0l) {
            return false;
        }
        return success(jedis.pexpire(key, milliseconds));
    }

    /**
     * 移除key的过期时间(使key持久保持)
     *
     * @param key key
     * @return boolean
     */
    public boolean persist(String key) {
        long ttl = ttl(key);
        if (ttl == FOREVER_EXIST) {
            return true;
        } else if (ttl == NOT_EXIST) {
            return false;
        }

        return success(jedis.persist(key));
    }

    /**
     * 删除key
     * 成功删除返回1，不存在则忽略并返回0
     *
     * @param key
     * @return long
     */
    public long delete(String key) {
        return jedis.del(key);
    }

    /**
     * 查找符合pattern的列表
     *
     * @param pattern 模式
     * @return java.util.Set
     */
    public Set<String> keys(String pattern) {
        return Optional.ofNullable(jedis.keys(pattern)).orElse(new HashSet<>());
    }

    /**
     * 容量大小
     *
     * @param key key
     * @return long
     */
    public long size(String key) {
        String type = type(key);
        if (TypeEnum.isInvalid(type)) {
            return 0L;
        } else if (TypeEnum.isString(type)) {
            return 1L;
        } else if (TypeEnum.isList(type)) {
            return jedis.llen(key);
        } else if (TypeEnum.isSet(type)) {
            return jedis.scard(key);
        } else if (TypeEnum.isSortedSet(type)) {
            return jedis.zcard(key);
        } else if (TypeEnum.isHash(type)) {
            return jedis.hlen(key);
        }
        return 0L;
    }

    public boolean success(String str) {
        return OK.equalsIgnoreCase(str);
    }

    public boolean success(Long val) {
        return SUCCESS == Optional.ofNullable(val).orElse(0L);
    }

    public RedisAccess getRedisAccess() {
        return redisAccess;
    }

    public void setRedisAccess(RedisAccess redisAccess) {
        this.redisAccess = redisAccess;
    }

    public void conn() {
        if (jedis == null) {
            jedis = redisAccess.getJedisPool().getResource();
            // System.out.println(" redis has connected.");
        }
    }

    public void close() {
        if (jedis != null) {
            jedis.close();
            // System.out.println(" redis has closed.");
        }
    }

}
