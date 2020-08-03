package com.elv.frame.exception;

import com.elv.frame.itf.IStatusCode;

/**
 * @author lxh
 * @since 2020-07-27
 */
public class BusinessException extends AbstractException {

    private static final long serialVersionUID = 6828487653211000130L;

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(int code, String msg) {
        super(code, msg);
    }

    public BusinessException(int code, Exception e) {
        super(code, e);
    }

    public BusinessException(IStatusCode statusCode) {
        super(statusCode);
    }

    public BusinessException(String msg, Exception e) {
        super(msg, e);
    }
}

