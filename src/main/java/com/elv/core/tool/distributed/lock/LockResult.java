package com.elv.core.tool.distributed.lock;

/**
 * @author lxh
 * @since 2021-01-04
 */
public class LockResult extends LockParam {

    private boolean locked;

    private DistributedLockProxy distributedLockProxy;

    public void unlock() {
        if (locked && distributedLockProxy != null) {
            distributedLockProxy.unlock(getKey(), getValue());
            // System.out.println("释放了锁，" + gextKey() + "_" + Dater.of(timestamp() + "").dateTimeStr());
        } else {
            // System.out.println("未释放锁，" + getKey() + "_" + Dater.of(timestamp() + "").dateTimeStr());
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public DistributedLockProxy getDistributedLockProxy() {
        return distributedLockProxy;
    }

    public void setDistributedLockProxy(DistributedLockProxy distributedLockProxy) {
        this.distributedLockProxy = distributedLockProxy;
    }
}
