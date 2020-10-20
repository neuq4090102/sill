package com.elv.frame.exception;

/**
 * @author lxh
 * @since 2020-08-26
 */
public class RedisException extends AbstractException {

    private static final long serialVersionUID = 7857816632403500544L;

    public RedisException(String msg) {
        super(msg);
    }

    public RedisException(String msg, Exception e) {
        super(msg, e);
    }
}
