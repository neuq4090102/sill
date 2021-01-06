package com.elv.traning.db.redis;

import com.elv.core.tool.distributed.lock.LockResult;
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
        // System.out.println(redisCache.get("abcddd") == null);
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

        // TimeZoneCache timeZoneCache = context.getBean(TimeZoneCache.class);
        // if (timeZoneCache != null) {
        //     timeZoneCache.add(441L, 9);
        //     Integer integer = timeZoneCache.fetch(441L);
        //     System.out.println("timeZoneCache=" + integer);
        //

        // RedisLock redisLock = context.getBean(RedisLock.class);
        // boolean lock = redisLock.lock("aaaaaa", "aadd", 300000);
        // System.out.println(lock);
        // redisLock.unlock("aaaaaa", "aadd");

        long orderId = 111L;
        OrderLock orderLock = context.getBean(OrderLock.class);
        T1 t1 = new T1(orderLock);
        t1.exec(orderId);
        T2 t2 = new T2(orderLock);
        t2.exec(orderId);
    }

    class T1 extends Thread {
        private OrderLock orderLock;

        public T1(OrderLock orderLock) {
            this.orderLock = orderLock;
        }

        public void exec(long orderId) {
            LockResult lockResult = orderLock.lock(orderId, 300000);
            if (lockResult.isLocked()) {
                System.out.println("锁单成功");
                System.out.println("订单处理完了");
            } else {
                System.out.println("锁单失败");
            }
        }
    }

    class T2 extends Thread {
        private OrderLock orderLock;

        public T2(OrderLock orderLock) {
            this.orderLock = orderLock;
        }

        public void exec(long orderId) {
            LockResult lockResult = orderLock.lock(orderId, 300000);
            if (lockResult.isLocked()) {
                System.out.println("锁单成功");
                System.out.println("订单处理完了");
            } else {
                System.out.println("锁单失败");
            }
            lockResult.unlock();
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
