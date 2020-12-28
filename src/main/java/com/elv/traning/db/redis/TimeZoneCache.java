package com.elv.traning.db.redis;

import org.springframework.stereotype.Component;

import com.elv.core.tool.db.redis.AbstractCache;

/**
 * @author lxh
 * @since 2020-12-28
 */
@Component
public class TimeZoneCache extends AbstractCache {

    public void add(long hotelId, Integer integer) {
        super.add(hotelId + "", integer);
    }

    public Integer fetch(long hotelId) {
        return super.get(hotelId + "", Integer.class);
    }

    @Override
    protected String getKeyPrefix() {
        return "time_zone_";
    }

    @Override
    protected int ttl() {
        return 1000;
    }

}
