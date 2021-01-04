package com.elv.core.tool.distributed.lock;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

/**
 * @author lxh
 * @since 2020-12-30
 */
public class DistributedLockProxy implements IDistributedLock {

    @Resource
    private IDistributedLock distributedLock;

    public boolean lock(String key, String value) {
        return distributedLock.lock(key, value, Long.MAX_VALUE);
    }

    @Override
    public boolean lock(String key, String value, long milliseconds) {
        return distributedLock.lock(key, value, milliseconds);
    }

    public boolean lock(String key, String value, long milliseconds, int loops, long waitMs) {
        int times = loops;
        while (times > 0) {
            try {
                boolean lock = distributedLock.lock(key, value, milliseconds);
                if (lock) {
                    return true;
                }

                if (waitMs > 0) {
                    TimeUnit.MILLISECONDS.sleep(waitMs);
                }
            } catch (Exception e) {
                // do-nothing
            } finally {
                times--;
            }
        }

        return false;
    }

    @Override
    public void unlock(String key, String value) {
        distributedLock.unlock(key, value);
    }

    public IDistributedLock getDistributedLock() {
        return distributedLock;
    }

    public void setDistributedLock(IDistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }
}
