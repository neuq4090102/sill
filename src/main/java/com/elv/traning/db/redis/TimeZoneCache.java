package com.elv.traning.db.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.elv.core.tool.cache.AbstractRedisCache;

/**
 * @author lxh
 * @since 2020-12-28
 */
@Component
public class TimeZoneCache extends AbstractRedisCache {

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
        return 100;
    }

    public static void main(String[] args) {
        lock("1", "1", 1000L, 3, 1000L);
    }

    public static boolean lock(String key, String value, long milliseconds, int loops, long waitMs) {
        int times = loops;
        while (times > 0) {
            try {
                boolean lock = 1 == 2;
                if (lock) {
                    return true;
                }

                if (waitMs > 0) {
                    TimeUnit.MILLISECONDS.sleep(waitMs);
                }
                Object object = null;
                object.getClass();
            } catch (Exception e) {
            } finally {
                times--;
            }
        }

        return false;
    }

}
