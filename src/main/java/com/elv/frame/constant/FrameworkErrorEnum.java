package com.elv.frame.constant;

import com.elv.frame.common.IStatusCodeEnum;

/**
 * @author lxh
 * @date 2020-03-25
 */
public enum FrameworkErrorEnum implements IStatusCodeEnum {

    PARAM_ERROR(-100, "参数检查错误"),  //
    ;

    private final int code;
    private final String msg;

    private FrameworkErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
