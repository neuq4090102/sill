package com.elv.core.tool.db.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.elv.core.annotation.redis.Redis;
import com.elv.core.constant.RedisEnum.TypeEnum;
import com.elv.core.util.Assert;
import com.elv.core.util.JsonUtil;

import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * @author lxh
 * @since 2020-08-25
 */
public class RedisQueue extends BaseRedis {

    /**
     * 队列内容
     *
     * @param key key
     * @return java.util.List
     */
    @Redis
    public List<String> queue(String key) {
        if (!exists(key)) {
            return Collections.emptyList();
        }
        return jedis.lrange(key, 0, Long.MAX_VALUE);
    }

    /**
     * 入队列
     *
     * @param key key
     * @param ts  入队参数
     * @param <T> 范型
     * @return boolean
     */
    @Redis
    public <T> boolean enqueue(String key, T... ts) {
        if (exists(key)) {
            Assert.isTrue(TypeEnum.isNotList(type(key)), "");
        }

        String[] inputs = new String[ts.length];
        for (int i = 0; i < ts.length; i++) {
            T t = ts[i];
            if (t instanceof String) {
                inputs[i] = (String) t;
            } else {
                inputs[i] = JsonUtil.toJson(t);
            }
        }
        jedis.rpush(key, inputs);

        return true;
    }

    /**
     * 出队列
     *
     * @param key key
     * @return java.lang.String
     */
    @Redis
    public String dequeue(String key) {
        if (TypeEnum.isNotList(type(key))) {
            return "";
        }
        return jedis.lpop(key);
    }

    /**
     * 出队列
     *
     * @param key   key
     * @param count 出队数量
     * @return java.util.List
     */
    @Redis
    public List<String> dequeue(String key, long count) {
        if (TypeEnum.isNotList(type(key))) {
            return Collections.emptyList();
        }

        long loops = Math.max(count, 1);
        loops = Math.min(loops, jedis.llen(key));

        List<Response<String>> results = new ArrayList<>();

        // redis事务
        Transaction multi = jedis.multi(); // 开始
        for (int i = 0; i < loops; i++) {
            /**
             * 此处如果使用：results.add(multi.lpop(key).get());
             * 将会报错：Please close pipeline or multi block before calling this method
             * 原因：Redis事务的执行结果是在exec之后统一返回的，事务执行中，Jedis用Response对象缓存返回值，
             * 如果在事务执行exec方法之前调用Response对象的get方法就会出现上述异常
             */
            results.add(multi.lpop(key));
        }
        multi.exec(); // 结束

        return results.stream().map(item -> item.get()).collect(Collectors.toList());
    }
}
