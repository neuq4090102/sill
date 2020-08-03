package com.elv.web.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elv.core.util.StrUtil;

/**
 * @author lxh
 * @since 2020-03-23
 */
public class RequestUtil {

    private RequestUtil() {

    }

    public static int getIntParam(String name) {
        return getIntParam(name, 0);
    }

    public static int getIntParam(String name, int defaultValue) {
        return (int) getDoubleParam(name, defaultValue);
    }

    public static long getLongParam(String name) {
        return getLongParam(name, 0L);
    }

    public static long getLongParam(String name, long defaultValue) {
        String parameter = getStringParam(name);
        if (!StrUtil.isDigit(parameter)) {
            return defaultValue;
        }

        return Long.parseLong(parameter);
    }

    public static double getDoubleParam(String param) {
        return getDoubleParam(param, 0D);
    }

    public static double getDoubleParam(String param, double defaultValue) {
        // 注意此处是先将double转化为字符串再生成BigDecimal,防数据丢失
        BigDecimal bigDecimal = getBigDecimalParam(param, new BigDecimal(defaultValue + ""));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }

    public static BigDecimal getBigDecimalParam(String name, BigDecimal defaultValue) {
        String parameter = getStringParam(name);
        if (!StrUtil.isDigit(parameter)) {
            return defaultValue;
        }

        BigDecimal bigDecimal = new BigDecimal(parameter);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

        return bigDecimal;
    }

    public static String getStringParam(String name) {
        return getStringParam(name, null);
    }

    public static String getStringParam(String name, String defaultValue) {
        String parameter = getRequest().getParameter(name);
        if (parameter == null) {
            return defaultValue;
        }

        return parameter.trim();
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
