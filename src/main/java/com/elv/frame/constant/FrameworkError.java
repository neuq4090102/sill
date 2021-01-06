package com.elv.frame.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.elv.frame.itf.IStatusCode;

/**
 * @author lxh
 * @since 2020-03-25
 */
public enum FrameworkError implements IStatusCode {

    COMMON_ERROR(-100, "通用异常错误"),  //
    PARAM_ERROR(-101, "参数检查错误"),  //
    CONCURRENT_ERROR(-102, "并发操作，请刷新后重试"),  //
    ;

    private static List<Integer> ignoreLogs;

    static {
        ignoreLogs = Arrays.stream(FrameworkError.values()).filter(item -> item != COMMON_ERROR)
                .map(item -> item.getCode()).collect(Collectors.toList());
    }

    private final int code;
    private final String msg;

    FrameworkError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static boolean isNotPrintLog(Integer code) {
        return ignoreLogs.contains(code);
    }
}
