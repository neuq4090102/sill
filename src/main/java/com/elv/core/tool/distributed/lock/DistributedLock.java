package com.elv.core.tool.distributed.lock;

import com.elv.core.annotation.redis.Redis;
import com.elv.core.tool.db.redis.RedisCache;

import redis.clients.jedis.params.SetParams;

/**
 * @author lxh
 * @since 2020-12-30
 */
public class DistributedLock extends RedisCache implements IDistributedLock {

    // @Redis
    // @Override
    // public boolean lock(String key, long milliseconds) {
    //     return success(jedis.set(key,
    //             JsonUtil.toJson(LockValue.of().timestamp(Dater.now().timestamp()).milliseconds(milliseconds)),
    //             SetParams.setParams().nx().px(milliseconds)));
    // }
    //
    // @Redis
    // @Override
    // public void unlock(String key) {
    //     String oldValue = jedis.get(key);
    //     if (oldValue == null) {
    //         return;
    //     }
    //
    //     System.out.println("oldValue=" + oldValue);
    //     LockValue lockValue = JsonUtil.toObject(oldValue, LockValue.class);
    //     if (lockValue == null) {
    //         return;
    //     }
    //
    //     System.out.println("timestamp=" + lockValue.timestamp());
    //     System.out.println("milliseconds=" + lockValue.getMilliseconds());
    //
    //     long millis = Duration.between(Dater.of(lockValue.timestamp() + "").instant(), Instant.now()).toMillis();
    //     System.out.println("millis=" + millis);
    //     if (millis > lockValue.getMilliseconds()) { // 防止先锁释放后锁
    //         return;
    //     }
    //
    //     jedis.del(key);
    // }

    @Redis
    @Override
    public boolean lock(final String key, final String value, final long milliseconds) {
        return success(jedis.set(key, value, SetParams.setParams().nx().px(milliseconds)));
    }

    @Redis
    @Override
    public void unlock(final String key, final String value) {
        String oldValue = jedis.get(key);
        if (oldValue == null) {
            return;
        } else if (!oldValue.equals(value)) { // 非相同value，不再解锁
            return;
        }
        jedis.del(key);
    }

}
