package com.elv.traning.db.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.elv.core.tool.db.redis.RedisCache;
import com.elv.core.tool.db.redis.RedisQueue;
import com.elv.core.tool.frame.ContextLauncher;
import com.elv.core.util.JsonUtil;

import redis.clients.jedis.Jedis;

/**
 * @author lxh
 * @since 2020-06-16
 */
public class RedisUtil extends ContextLauncher {

    private static long NOT_EXIST = -2L;
    private static String SUCCESS = "OK";

    private static Jedis jedis;

    static {
        jedis = new Jedis("localhost");
    }

    /**
     * 配置VM参数：-Dspring.profiles.active=dev
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.deal();

        // testConn();
        // testKey();
        // testString();
        // testHash();
        //
        // jedis.close();

    }

    private static void testHash() {
        // RedisHash.family("hash", "hk");
        // RedisList.family("list");
        RedisSet.family("set");
    }

    private static void testString() {

        RedisString.family("ttt");
    }

    public static void testKey() {
        // long ttl = RedisKey.ttl("ab");
        // System.out.println(ttl);

        RedisKey.family("abc");

    }

    public static void testConn() {
        RedisConn.conn();
    }

    @Override
    protected void exec() {
        //得到类的实例
        // RedisCache redisCache = context.getBean(RedisCache.class);
        // Jedis jedis = redisCache.getRedisAccess().conn();
        RedisCache redisCache = context.getBean(RedisCache.class);
        System.out.println(redisCache.get("abc"));
        System.out.println(redisCache.exists("ttt"));


        RedisQueue redisQueue = context.getBean(RedisQueue.class);
        String sms = "sms2";
        for (int i = 0; i < 0; i++) {
            redisQueue.enqueue(sms, "1", 2, null, "abc", 455, "dkdk", "ldf");
            redisQueue.enqueue(sms, "1");
        }
        System.out.println(redisQueue.size(sms));
        System.out.println(JsonUtil.toJson(redisQueue.queue(sms)));
        System.out.println(JsonUtil.toJson(redisQueue.dequeue(sms, 3)));
        System.out.println(JsonUtil.toJson(redisQueue.queue(sms)));
    }

    private static class RedisConn {
        public static void conn() {
            //连接本地的 Redis 服务
            System.out.println("连接成功");
            //查看服务是否运行
            System.out.println("服务正在运行: " + jedis.ping());
            System.out.println(jedis.get("abc"));
        }
    }

    private static class RedisKey {

        public static void family(String key) {
            System.out.println("dbSize:" + dbSize());
            System.out.println("exists:" + exists(key));
            System.out.println("type:" + type(key));
            System.out.println("ttl:" + ttl(key));
            System.out.println("pttl:" + pttl(key));
            // System.out.println("del:" + del(key));
            keys("*").stream().forEach(item -> System.out.println("keys:" + item));

            System.out.println("rename:" + rename("ttt2", "ttt"));

            // System.out.println("set:" + RedisString.set(key, "abc"));
            // System.out.println("expire:" + expire(key, 3600));
            // System.out.println("pexpire:"+pexpire(key, 2000000));
            // System.out.println("persist:" + persist(key));

        }

        /**
         * 当前数据库中key的个数
         *
         * @return long
         */
        public static long dbSize() {
            return Optional.ofNullable(jedis.dbSize()).orElse(0L);
        }

        /**
         * key是否存在
         *
         * @param key key
         * @return boolean
         */
        public static boolean exists(String key) {
            return Optional.ofNullable(jedis.exists(key)).orElse(false);
        }

        /**
         * 储值类型
         * 不存在则返回"none"
         *
         * @param key key
         * @return java.lang.String
         */
        public static String type(String key) {
            return jedis.type(key);
        }

        /**
         * 以秒为单位，返回key的剩余生存时间
         * 当key不存在时，返回-2，当key存在但没有设置剩余生存时间时，返回-1
         *
         * @param key key
         * @return long
         */
        public static long ttl(String key) {
            return Optional.ofNullable(jedis.ttl(key)).orElse(NOT_EXIST);
        }

        /**
         * 以毫秒为单位，返回key的剩余生存时间
         * 当key不存在时，返回-2，当key存在但没有设置剩余生存时间时，返回-1
         *
         * @param key key
         * @return long
         */
        public static long pttl(String key) {
            return Optional.ofNullable(jedis.pttl(key)).orElse(NOT_EXIST);
        }

        /**
         * 以秒为单位给key设置过期时间
         * 设置成功返回1，当key不存在或者设置失败则返回0
         *
         * @param key     key
         * @param seconds 秒数
         * @return long
         */
        public static long expire(String key, int seconds) {
            return jedis.expire(key, seconds);
        }

        /**
         * 以毫秒为单位给key设置过期时间
         * 设置成功返回1，当key不存在或者设置失败则返回0
         *
         * @param key          key
         * @param milliseconds 毫秒数
         * @return long
         */
        public static long pexpire(String key, long milliseconds) {
            return jedis.pexpire(key, milliseconds);
        }

        /**
         * 移除key的过期时间，key将持久保持
         * 移除成功时返回1；当key不存在没有设置过期时间返回0
         *
         * @param key key
         * @return long
         */
        public static long persist(String key) {
            return jedis.persist(key);
        }

        /**
         * 删除key
         * 成功删除返回1，不存在则忽略并返回0
         *
         * @param key key
         * @return long
         */
        public static long del(String key) {
            return jedis.del(key);
        }

        /**
         * 查找符合pattern的列表
         *
         * @param pattern 模式
         * @return java.util.Set
         */
        public static Set<String> keys(String pattern) {
            return Optional.ofNullable(jedis.keys(pattern)).orElse(new HashSet<>());
        }

        /**
         * 重命名key
         *
         * @param oldKey 旧key
         * @param newKey 新key
         * @return boolean
         */
        public static boolean rename(String oldKey, String newKey) {
            if (!exists(oldKey)) {
                return false;
            }
            jedis.rename(oldKey, newKey);

            return true;
        }

    }

    public static class RedisString {

        public static void family(String key) {
            System.out.println("set:" + set(key, "777"));
            System.out.println("get:" + get(key));
            System.out.println("getSet:" + getSet(key, "888t"));
            System.out.println("incr:" + incr(key));
            System.out.println("incr:" + incrBy(key, 3));
            System.out.println("decr:" + decr(key));
            System.out.println("decr:" + decrBy(key, 2));
            System.out.println("append:" + append(key, "999"));
            System.out.println("length:" + length(key));
        }

        /**
         * 储值
         *
         * @param key   key
         * @param value 值
         * @return java.lang.String
         */
        public static String set(String key, String value) {
            return jedis.set(key, value);
        }

        /**
         * 取值
         *
         * @param key key
         * @return java.lang.String
         */
        public static String get(String key) {
            return jedis.get(key);
        }

        /**
         * 返回旧值并赋予新值
         *
         * @param key   key
         * @param value 值
         * @return java.lang.String
         */
        public static String getSet(String key, String value) {
            return jedis.getSet(key, value);
        }

        /**
         * key中储值+1
         * <p>
         * key不存在，则初始化并+1；
         * 储值不能转化为数字类型，将返回异常
         *
         * @param key key
         * @return long
         */
        public static long incr(String key) {
            return jedis.incr(key);
            // return Optional.ofNullable(jedis.incr(key)).orElse(0L);
        }

        /**
         * key中储值+increment
         *
         * @param key       key
         * @param increment 增量
         * @return long
         * @see #incr(String)
         */
        public static long incrBy(String key, long increment) {
            return jedis.incrBy(key, increment);
            // return Optional.ofNullable(jedis.incr(key)).orElse(0L);
        }

        /**
         * key中储值-1
         * <p>
         * key不存在，则初始化并-1；
         * 储值不能转化为数字类型，将返回异常
         *
         * @param key key
         * @return long
         */
        public static long decr(String key) {
            return jedis.decr(key);
            // return Optional.ofNullable(jedis.decr(key)).orElse(0L);
        }

        /**
         * key中储值-decrement
         *
         * @param key       key
         * @param decrement 减量
         * @return long
         * @see #decr
         */
        public static long decrBy(String key, long decrement) {
            return jedis.decrBy(key, decrement);
            // return Optional.ofNullable(jedis.decr(key)).orElse(0L);
        }

        /**
         * 追加value
         * <p>
         * key不存在，则执行set
         *
         * @param key   key
         * @param value 值
         * @return long 追加value后的字符串总长度
         */
        public static long append(String key, String value) {
            return jedis.append(key, value);
            // return Optional.ofNullable(jedis.append(key, value)).orElse(0L);
        }

        /**
         * 储值长度
         *
         * @param key key
         * @return long
         */
        public static long length(String key) {
            return jedis.strlen(key);
            // return Optional.ofNullable(jedis.append(key, value)).orElse(0L);
        }
    }

    public static class RedisHash {
        public static void family(String key, String field) {

            Map<String, String> map = new HashMap<>();
            map.put("1", "12");
            map.put("2", "23");

            System.out.println("hexists：" + jedis.hexists(key, field));
            System.out.println("hset-1：" + jedis.hset(key, field, "value"));
            System.out.println("hset：" + jedis.hget(key, field));
            System.out.println("hset-map：" + jedis.hset(key, map));
            Map<String, String> stringStringMap = jedis.hgetAll(key);
            stringStringMap.forEach((key1, value) -> System.out.println(key1 + ":" + value));
            jedis.hkeys(key).stream().forEach(item -> System.out.println("key:" + item));
            jedis.hvals(key).stream().forEach(item -> System.out.println("value:" + item));
            System.out.println(jedis.hlen(key));
        }
    }

    public static class RedisList {
        public static void family(String key) {

            System.out.println("lpush：" + jedis.lpush(key, "222", "333"));
            // System.out.println("lset：" + jedis.lset(key, 0, "111"));
            System.out.println("rpush：" + jedis.rpush(key, "aaa", "bbb"));
            System.out.println("lpop：" + jedis.lpop(key));
            jedis.lrange(key, 0, Integer.MAX_VALUE).stream().forEach(item -> System.out.println("item:" + item));
            ;
            System.out.println("llen：" + jedis.llen(key));
            System.out.println(jedis.clientList());
        }
    }

    public static class RedisSet {
        public static void family(String key) {
            System.out.println("sadd：" + jedis.sadd(key, "222", "333"));
            System.out.println("spop：" + jedis.spop(key));
            System.out.println("scard：" + jedis.scard(key));

        }
    }
}
