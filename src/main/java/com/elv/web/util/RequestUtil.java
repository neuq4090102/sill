package com.elv.web.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elv.core.util.StrUtil;

/**
 * 参数请求工具
 *
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
        String parameter = getStrParam(name);
        if (!StrUtil.isDigit(parameter)) {
            return defaultValue;
        }

        return Long.parseLong(parameter);
    }

    public static double getDoubleParam(String param) {
        return getDoubleParam(param, 0D);
    }

    public static double getDoubleParam(String param, double defaultValue) {
        // 注意：此处是先将double转化为字符串再生成BigDecimal,防数据丢失
        BigDecimal bigDecimal = getBigDecimalParam(param, new BigDecimal(defaultValue + ""));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }

    public static BigDecimal getBigDecimalParam(String name, BigDecimal defaultValue) {
        String parameter = getStrParam(name);
        if (!StrUtil.isDigit(parameter)) {
            return defaultValue;
        }
        return new BigDecimal(parameter).setScale(2, RoundingMode.HALF_UP);
    }

    public static String getStrParam(String name) {
        return getStrParam(name, null);
    }

    public static String getStrParam(String name, String defaultValue) {
        String parameter = getRequest().getParameter(name);
        if (parameter == null) {
            return defaultValue;
        }

        return parameter.trim();
    }

    public static String getIp() {
        return Request.getIp(getRequest());
    }

    public static List<String> getAllParams() {
        //TODO
        Enumeration<String> parameterNames = getRequest().getParameterNames();

        return null;
    }

    public static void md5(String secretKey) {
    }

    public static void mdBy(String algorithm) {

        // return SecurityUtil.mdBy();
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static class Request {

        private static String getIp(HttpServletRequest request) {
            return request.getRemoteAddr();
        }



    }

}
