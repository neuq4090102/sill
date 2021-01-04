package com.elv.core.tool.distributed.lock;

/**
 * @author lxh
 * @since 2021-01-04
 */
public class LockParam {

    private String key; // 键
    private String value; // 值
    private long timestamp; // 加锁时间戳
    private long milliseconds; // 锁时长

    public LockParam() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public static LockParam of() {
        return new LockParam();
    }

    public LockParam key(String key) {
        this.key = key;
        return this;
    }

    public LockParam value(String value) {
        this.value = value;
        return this;
    }

    public LockParam timestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public LockParam milliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
        return this;
    }
}
