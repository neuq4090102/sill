package com.elv.frame.constant;

import com.elv.frame.itf.IStatusCode;

/**
 * @author lxh
 * @since 2020-03-20
 */
public enum StatusCode implements IStatusCode {
    SUCCESS(0, "success"),  //
    FAIL(-1, "fail"), //
    ;

    private final int code;
    private final String msg;

    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public static boolean isSuccess(int code) {
        return code == SUCCESS.getCode();
    }
}
