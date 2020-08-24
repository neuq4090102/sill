package com.elv.frame.exception;

import com.elv.frame.itf.IStatusCode;

/**
 * @author lxh
 * @since 2020-07-27
 */
public abstract class AbstractException extends RuntimeException {

    private static final long serialVersionUID = 6235387673487292529L;

    private int code;
    private String msg = "";
    private Object error; // 异常数据

    public AbstractException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public AbstractException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public AbstractException(int code, Exception e) {
        super(e);
        this.code = code;
    }

    public AbstractException(IStatusCode statusCode) {
        super(statusCode.getMsg());
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public AbstractException(IStatusCode statusCode, Object error) {
        super(statusCode.getMsg());
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.error = error;
    }

    public AbstractException(String msg, Exception e) {
        super(msg);
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
