package com.elv.core.tool.cache;

import java.util.Optional;

import javax.annotation.Resource;

import com.elv.core.tool.distributed.lock.DistributedLockProxy;
import com.elv.core.tool.distributed.lock.LockParam;
import com.elv.core.tool.distributed.lock.LockResult;
import com.elv.core.util.Dater;

/**
 * @author lxh
 * @since 2020-12-31
 */
public abstract class AbstractDistributedLock {

    private final static String LOCK_DEFAULT_VALUE = "LDV";

    @Resource
    protected DistributedLockProxy distributedLockProxy;

    public boolean lock(String key, String value, long milliseconds) {
        return distributedLockProxy.lock(key, Optional.ofNullable(value).orElse(LOCK_DEFAULT_VALUE), milliseconds);
    }

    public void unlock(String key, String value) {
        distributedLockProxy.unlock(key, Optional.ofNullable(value).orElse(LOCK_DEFAULT_VALUE));
    }

    protected LockResult lock(LockParam lockParam) {
        boolean lock = this.lock(lockParam.getKey(), lockParam.getValue(), lockParam.getMilliseconds());
        LockResult lockResult = new LockResult();
        lockResult.setLocked(lock);
        if (lock) {
            lockResult.setKey(lockParam.getKey());
            lockResult.setValue(lockParam.getValue());
            lockResult.setTimestamp(Dater.now().ts());
            lockResult.setMilliseconds(lockParam.getMilliseconds());
            lockResult.setDistributedLockProxy(this.distributedLockProxy);
        }
        return lockResult;
    }

}
