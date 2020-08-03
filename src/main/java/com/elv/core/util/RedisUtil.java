package com.elv.core.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.elv.core.model.RedisCache;

import redis.clients.jedis.Jedis;

/**
 * @author lxh
 * @since 2020-06-16
 */
public class RedisUtil {

    /**
     * 配置VM参数：-Dspring.profiles.active=dev
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        test();
        // startWithConfig();
    }

    private static void startWithConfig() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:META-INF/spring/applicationContext.xml");
        //得到类的实例
        RedisCache redisCache = (RedisCache) context.getBean(RedisCache.class);
        Jedis jedis = redisCache.conn();
        System.out.println(jedis.get("abc"));

        ClassPathXmlApplicationContext en = (ClassPathXmlApplicationContext) context;
        en.close();
    }

    public static void test() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());
        System.out.println(jedis.get("abc"));
    }
}
