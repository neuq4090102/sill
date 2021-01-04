package com.elv.traning.db.redis;

import org.springframework.stereotype.Component;

import com.elv.core.tool.cache.AbstractDistributedLock;

/**
 * @author lxh
 * @since 2020-12-30
 */
@Component
public class OrderLock extends AbstractDistributedLock {

    public boolean lock(long orderId, long ms) {
        return lock("order_id_" + orderId, Thread.currentThread().getId() + "", ms);
    }

    public void unlock(long orderId) {
        unlock("order_id_" + orderId, Thread.currentThread().getId() + "");
    }

}
