package com.elv.traning.db.redis;

import org.springframework.stereotype.Component;

import com.elv.core.tool.cache.AbstractDistributedLock;
import com.elv.core.tool.distributed.lock.LockParam;
import com.elv.core.tool.distributed.lock.LockResult;

/**
 * @author lxh
 * @since 2020-12-30
 */
@Component
public class OrderLock extends AbstractDistributedLock {

    public LockResult lock(long orderId, long milliseconds) {
        return lock(LockParam.of().key("order_id_" + orderId).milliseconds(milliseconds));
    }

    public boolean locked(long orderId, long milliseconds) {
        return lock("order_id_" + orderId, null, milliseconds);
    }

    public void unlock(long orderId) {
        unlock("order_id_" + orderId, null);
    }

}
