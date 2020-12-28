package com.elv.traning.db.redis;

import com.elv.core.tool.frame.ContextLauncher;

import redis.clients.jedis.Jedis;

/**
 * @author lxh
 * @since 2020-06-16
 */
public class RedisUtil extends ContextLauncher {

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
    }

    @Override
    protected void exec() {
        // 得到类的实例
        // RedisCache redisCache = context.getBean(RedisCache.class);
        // System.out.println(redisCache.get("abc"));
        // System.out.println(redisCache.exists("ttt"));

        // RedisQueue redisQueue = context.getBean(RedisQueue.class);
        // String sms = "sms2";
        // for (int i = 0; i < 0; i++) {
        //     redisQueue.enqueue(sms, "1", 2, null, "abc", 455, "dkdk", "ldf");
        //     redisQueue.enqueue(sms, "1");
        // }
        // System.out.println(redisQueue.size(sms));
        // System.out.println(JsonUtil.toJson(redisQueue.queue(sms)));
        // System.out.println(JsonUtil.toJson(redisQueue.dequeue(sms, 3)));
        // System.out.println(JsonUtil.toJson(redisQueue.queue(sms)));

        TimeZoneCache timeZoneCache = context.getBean(TimeZoneCache.class);
        if (timeZoneCache != null) {
            timeZoneCache.add(441L, 8);
            Integer integer = timeZoneCache.fetch(441L);
            System.out.println("timeZoneCache=" + integer);
        }
    }

    public static void testConn() {
        RedisConn.conn();
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

}
