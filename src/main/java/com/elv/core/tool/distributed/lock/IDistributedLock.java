package com.elv.core.tool.distributed.lock;

/**
 * @author lxh
 * @since 2020-12-30
 */
public interface IDistributedLock {

    /**
     * 加锁
     *
     * @param key          键
     * @param value        值
     * @param milliseconds 毫秒数
     * @return boolean
     */
    boolean lock(final String key, final String value, final long milliseconds);

    /**
     * 解锁
     *
     * @param key   键
     * @param value 值
     */
    void unlock(final String key, final String value);

}
