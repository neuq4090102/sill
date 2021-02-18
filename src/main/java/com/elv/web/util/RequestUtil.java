package com.elv.web.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.elv.core.constant.SecurityEnum.Algorithm;
import com.elv.core.constant.SortEnum;
import com.elv.core.util.SecurityUtil;
import com.elv.core.util.StrUtil;
import com.elv.core.util.Utils;
import com.elv.web.constant.RequestEnum.AgentEnum;
import com.elv.web.model.SignParam;

/**
 * 参数请求工具
 *
 * @author lxh
 * @since 2020-03-23
 */
public class RequestUtil {

    private static final String SIGN; // 签名，注意：是消息摘要，而非数字签名
    private static final String SIGN_TYPE; // 签名算法，注意：是消息摘要算法，而非数字签名算法

    static {
        SIGN = "sign";
        SIGN_TYPE = "signType";
    }

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

    /**
     * <pre>
     *     /api/app/capsule/letter/query/by_id.json
     * </pre>
     *
     * @return java.lang.String
     */
    public static String getURI() {
        return Request.getURI(getRequest());
    }

    public static String getURI(HttpServletRequest request) {
        return Request.getURI(request);
    }

    /**
     * <pre>
     *      http://localhost:9090/api/app/capsule/letter/query/by_id.json
     * </pre>
     *
     * @return java.lang.String
     */
    public static String getURL() {
        return getRequest().getRequestURL().toString();
    }

    public static String getRemoteIP() {
        return Request.getRemoteIP(getRequest());
    }

    public static long getLongIP() {
        return Utils.ipToLong(getRemoteIP());
    }

    public static String getAgent() {
        return AgentEnum.fetch(Request.getHeader(getRequest(), "User-Agent"));
    }

    public static String getHeader(String headerName) {
        return Request.getHeader(getRequest(), headerName);
    }

    public static List<String> getAllParams() {
        return Request.getAllParams(getRequest(), null);
    }

    public static List<String> getAllParams(SortEnum sortEnum) {
        return Request.getAllParams(getRequest(), sortEnum);
    }

    public static Map<String, String> getParamsMap() {
        return Request.getParamsMap(getRequest(), null);
    }

    public static Map<String, String> getParamsMap(SortEnum sortEnum) {
        return Request.getParamsMap(getRequest(), sortEnum);
    }

    public static String md5(String secretKey) {
        return sign(Algorithm.MD5.getVal(), secretKey, "");
    }

    public static String sha256(String secretKey) {
        return sign(Algorithm.SHA256.getVal(), secretKey, "");
    }

    public static String sign(String algorithm, String secretKey, String secretName) {
        return sign(SignParam.of().paramMap(getParamsMap()).algorithm(algorithm).secretName(secretName)
                .secretKey(secretKey));

    }

    /**
     * 签名（请求参数摘要）
     *
     * @param signParam 签名参数
     * @return java.lang.String
     */
    public static String sign(SignParam signParam) {
        Map<String, String> paramMap = signParam.getParamMap();
        if (paramMap == null || paramMap.size() == 0) {
            return "";
        }
        String secretName = StrUtil.defaultIfBlank(signParam.getSecretName(), SIGN);
        paramMap.remove(secretName);

        SortEnum sortEnum = signParam.getSortEnum();
        if (sortEnum == SortEnum.DESC) {
            paramMap = paramMap.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByKey()))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        } else {
            paramMap = paramMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }

        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : paramMap.entrySet()) {
            String value = entry.getValue();
            if (StrUtil.isEmpty(value)) {
                continue;
            }
            sb.append(entry.getKey()).append("=").append(value).append("&");
        }
        sb.append(secretName).append("=").append(signParam.getSecretKey());

        return SecurityUtil.mdBy(sb.toString(), signParam.getAlgorithm());
    }

    /**
     * 校验签名
     *
     * @param algorithm  消息摘要算法，可空，默认sha256
     * @param secretKey  密钥，由服务方提供
     * @param secretName 自定义密钥参数名称
     * @return boolean
     */
    public static boolean checkSign(String algorithm, String secretKey, String secretName) {
        String sign = getStrParam(StrUtil.defaultIfBlank(secretName, SIGN));
        if (StrUtil.isEmpty(sign)) {
            return false;
        }
        String mdAlgorithm = StrUtil.defaultIfBlank(algorithm, Algorithm.SHA256.getVal());
        mdAlgorithm = StrUtil.defaultIfBlank(getStrParam(SIGN_TYPE), mdAlgorithm);

        return sign(mdAlgorithm, secretKey, secretName).equals(sign);
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static class Request {

        /**
         * 获取请求URI
         *
         * @param request 请求对象
         * @return java.lang.String
         */
        public static String getURI(HttpServletRequest request) {
            return new UrlPathHelper().getRequestUri(request);
        }

        /**
         * 获取请求IP
         *
         * @param request 请求对象
         * @return java.lang.String
         */
        public static String getRemoteIP(HttpServletRequest request) {
            String unknown = "unknown";
            String ip = request.getHeader("X-Forwarded-For"); // Squid服务代理
            if (StrUtil.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP"); // Nginx服务代理
            }
            if (StrUtil.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP"); // Apache服务代理
            }
            if (StrUtil.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP"); // WebLogic服务代理
            }
            if (StrUtil.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP"); // 其他服务代理
            }
            if (StrUtil.isNotBlank(ip)) {
                ip = Arrays.stream(ip.split(",")).filter(item -> !unknown.equalsIgnoreCase(item.trim())).findFirst()
                        .orElse(request.getRemoteAddr());
            } else {
                ip = request.getRemoteAddr();
            }

            // 0:0:0:0:0:0:0:1 是IPv6的本地ip，相当于IPv4的127.0.0.1
            return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
        }

        /**
         * 获取参数Map
         *
         * @param request  请求对象
         * @param sortEnum 排序参数，null不排序，ASC正序，DESC倒叙
         * @return java.util.Map
         */
        public static Map<String, String> getParamsMap(HttpServletRequest request, SortEnum sortEnum) {
            Map<String, String> paramsMap = new LinkedHashMap<>();
            List<String> params = getAllParams(request, sortEnum);
            for (String param : params) {
                String paramValue = request.getParameter(param);
                if (paramValue != null) {
                    paramsMap.put(param, paramValue.trim());
                }
            }
            return paramsMap;
        }

        /**
         * 获取所有参数
         *
         * @param request  请求对象
         * @param sortEnum 排序参数，null不排序，ASC正序，DESC倒叙
         * @return java.util.List
         */
        public static List<String> getAllParams(HttpServletRequest request, SortEnum sortEnum) {
            List<String> params = new ArrayList<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                params.add(parameterNames.nextElement());
            }

            if (sortEnum == SortEnum.ASC) {
                params.sort(Comparator.naturalOrder());
            } else if (sortEnum == SortEnum.DESC) {
                params.sort(Comparator.reverseOrder());
            }

            return params;
        }

        /**
         * 请求头信息
         *
         * @param request    请求对象
         * @param headerName 头属性
         * @return java.lang.String
         */
        public static String getHeader(HttpServletRequest request, String headerName) {
            return request.getHeader(headerName);
        }

    }

}
